package com.github.alice.ktx

import com.github.alice.ktx.api.dialog.DialogApi
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.middleware.MiddlewareType
import com.github.alice.ktx.fsm.models.FSMStrategy
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse
import com.github.alice.ktx.server.WebServer
import com.github.alice.ktx.server.WebServerListener
import com.github.alice.ktx.fsm.FSMContext
import com.github.alice.ktx.fsm.MutableFSMContext
import com.github.alice.ktx.fsm.impl.BaseFSMContext
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.storage.Storage
import com.github.alice.ktx.storage.apiStorage.EnableApiStorage
import com.github.alice.ktx.storage.impl.memoryStorage
import kotlinx.serialization.json.Json

/**
 * Создает экземпляр `Skill` с заданной конфигурацией.
 *
 * @param body Функция, принимающая объект `Skill.Builder` и выполняющая настройку.
 * Эта функция будет вызвана в контексте `Skill.Builder`.
 * @return Настроенный объект `Skill`.
 */
@AliceDsl
fun skill(body: Skill.Builder.() -> Unit): Skill = Skill.Builder().apply(body).build()

/**
 * Класс `Skill` представляет собой навык, который обрабатывает запросы и управляет состоянием.
 *
 * @property webServer Сервер для прослушивания запросов.
 * @property dispatcher Объект для управления обработчиками команд, мидлварами и обработчиками сетевых ошибок.
 */
class Skill internal constructor(
    private val webServer: WebServer,
    private val dispatcher: Dispatcher,
    private val dialogApi: DialogApi?,
    private val defaultFSMStrategy: FSMStrategy,
    private val fsmContext: (message: MessageRequest) -> FSMContext,
    private val enableApiStorage: Boolean = false
) {

    /**
     * Конструктор `Builder` для создания экземпляра `Skill`.
     */
    @AliceDsl
    class Builder {

        lateinit var webServer: WebServer

        var skillId: String? = null
        var json: Json = Json {
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        var dialogApi: DialogApi? = null
        var storage: Storage = memoryStorage()

        var defaultFSMStrategy: FSMStrategy = FSMStrategy.USER
        var fsmContext: (message: MessageRequest) -> FSMContext = { message ->
            BaseFSMContext(storage, defaultFSMStrategy, message, skillId)
        }

        internal var dispatcherConfiguration: Dispatcher.() -> Unit = { }

        fun build(): Skill {
            return Skill(
                webServer = webServer,
                dialogApi = dialogApi,
                defaultFSMStrategy = defaultFSMStrategy,
                fsmContext = fsmContext,
                enableApiStorage = storage.javaClass.isAnnotationPresent(EnableApiStorage::class.java),
                dispatcher = Dispatcher().apply(dispatcherConfiguration)
            )
        }
    }

    /**
     * Запускает сервер и начинает обработку входящих запросов.
     */
    fun run() {
        val webServerCallback = webServerResponseCallback()
        webServer.run(webServerCallback)
    }

    /**
     * Создает слушатель для обработки запросов и ошибок от веб-сервера.
     *
     * @return Реализованный объект `WebServerResponseListener`, который обрабатывает входящие сообщения и ошибки.
     */
    private fun webServerResponseCallback(): WebServerListener = object : WebServerListener {
        override suspend fun handleRequest(model: MessageRequest): MessageResponse? {
            val requestEnvironment = createRequestEnvironment(model)

            runMiddlewares(requestEnvironment, MiddlewareType.OUTER)?.let { return it }
            dispatcher.commandHandlers.forEach { handler ->
                if (handler.shouldHandle(requestEnvironment)) {
                    runMiddlewares(requestEnvironment, MiddlewareType.INNER)?.let { return it }
                    return handler.processRequest(requestEnvironment)
                }
            }
            return null
        }

        override suspend fun handleError(model: MessageRequest, exception: Exception): MessageResponse? {
            val request = createRequestEnvironment(model)
            dispatcher.networkErrorHandlers.forEach { errorHandler ->
                errorHandler.responseFailure(request, exception)?.let { response ->
                    return response
                }
            }
            return null
        }

        /**
         * Выполняет мидлвари указанного типа.
         *
         * @param type Тип мидлвари, который следует выполнить.
         * @return `MessageResponse?` — ответ от мидлвари, или `null`, если обработка не завершена.
         */
        private suspend fun runMiddlewares(
            request: ProcessRequestEnvironment,
            type: MiddlewareType
        ): MessageResponse? {
            dispatcher.middlewares[type]?.forEach { middleware ->
                middleware.process(request)?.let { response ->
                    return response
                }
            }
            return null
        }
    }

    private suspend fun createRequestEnvironment(message: MessageRequest): ProcessRequestEnvironment {
        val context = fsmContext(message)
        context.init()

        return object : ProcessRequestEnvironment {
            override val message: MessageRequest = message
            override val context: MutableFSMContext = context
            override val dialogApi: DialogApi? = this@Skill.dialogApi
            override val fsmStrategy: FSMStrategy = this@Skill.defaultFSMStrategy
            override val enableApiStorage: Boolean = this@Skill.enableApiStorage
        }
    }
}