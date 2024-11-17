package com.github.alice.ktx

import com.github.alice.ktx.api.dialog.DialogApi
import com.github.alice.ktx.middleware.MiddlewareType
import com.github.alice.ktx.models.FSMStrategy
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.request
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse
import com.github.alice.ktx.models.toEventRequest
import com.github.alice.ktx.server.WebServer
import com.github.alice.ktx.server.WebServerResponseListener
import com.github.alice.ktx.context.FSMContext
import com.github.alice.ktx.context.impl.BaseFSMContext
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
fun skill(body: Skill.Builder.() -> Unit): Skill = Skill.Builder().build(body)

/**
 * Класс `Skill` представляет собой навык, который обрабатывает запросы и управляет состоянием.
 *
 * @property webServer Сервер для прослушивания запросов.
 * @property dispatcher Объект для управления обработчиками команд, мидлварами и обработчиками сетевых ошибок.
 */
class Skill internal constructor(
    private val webServer: WebServer,
    private val dispatcher: Dispatcher
) {

    /**
     * Конструктор `Builder` для создания экземпляра `Skill`.
     */
    class Builder {

        var id: String? = null
        var json: Json = Json { ignoreUnknownKeys = true }
        var dialogApi: DialogApi? = null
        lateinit var webServer: WebServer
        var defaultFSMStrategy: FSMStrategy = FSMStrategy.USER
        internal var dispatcherConfiguration: Dispatcher.() -> Unit = { }

        var storage: Storage = memoryStorage()

        var fsmContext: (message: MessageRequest) -> FSMContext = { message ->
            BaseFSMContext(storage, defaultFSMStrategy, message, id)
        }

        internal fun build(body: Builder.() -> Unit): Skill {
            body()

            return Skill(
                webServer = webServer,
                dispatcher = Dispatcher(
                    fsmStrategy = defaultFSMStrategy,
                    dialogApi = dialogApi,
                    fsmContext = fsmContext,
                    enableApiStorage = storage.javaClass.isAnnotationPresent(EnableApiStorage::class.java)
                ).apply(dispatcherConfiguration)
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
    private fun webServerResponseCallback(): WebServerResponseListener = object : WebServerResponseListener {
        override suspend fun messageHandle(model: MessageRequest): MessageResponse? {
            val request = dispatcher.request(model)
            val eventRequest = request.toEventRequest()

            runMiddlewares(request, MiddlewareType.OUTER)?.let { return it }
            dispatcher.commandHandlers.forEach { handler ->
                if (handler.event(eventRequest)) {
                    runMiddlewares(request, MiddlewareType.INNER)?.let { return it }
                    return handler.handle(request)
                }
            }
            return null
        }

        override suspend fun responseFailure(model: MessageRequest, ex: Exception): MessageResponse? {
            val request = dispatcher.request(model)
            dispatcher.networkErrorHandlers.forEach { errorHandler ->
                errorHandler.responseFailure(request, ex)?.let { response ->
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
        private suspend fun runMiddlewares(request: Request, type: MiddlewareType): MessageResponse? {
            dispatcher.middlewares[type]?.forEach { middleware ->
                middleware.invoke(request)?.let { response ->
                    return response
                }
            }
            return null
        }
    }
}