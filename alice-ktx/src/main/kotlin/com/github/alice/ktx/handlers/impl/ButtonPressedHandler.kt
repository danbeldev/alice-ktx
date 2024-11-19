package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.handlers.environments.ShouldRequestEnvironment
import com.github.alice.ktx.models.request.RequestContentType
import com.github.alice.ktx.models.response.MessageResponse

@AliceDsl
data class ButtonPressedShouldHandleEnvironment(
    private val requestEnvironment: ShouldRequestEnvironment
): ShouldRequestEnvironment by requestEnvironment {

    val payload: Map<String, Any> = requestEnvironment.message.request.payload
}

/**
 * Функция расширения для `Dispatcher`, которая добавляет обработчик событий для нажатия кнопки.
 *
 * Навык получает запрос с типом ButtonPressed, если в предыдущем ответе пользователь нажал:
 *
 * 1. отдельную кнопку (свойство hide со значением true) с непустым полем payload;
 * 2. изображение (тип BigImage) с непустым полем payload в card.button;
 * 3. элемент списка (тип ItemList) с непустым полем payload в items.button;
 * 4. изображение из галереи (тип ImageGallery) с непустым полем payload в items.button.
 *
 * @param shouldHandle Логика, определяющая, должен ли обработчик сработать для данного события.
 * @param processRequest Логика обработки запроса, если событие было подтверждено.
 */
@AliceDsl
fun Dispatcher.buttonPressed(
    shouldHandle: suspend ButtonPressedShouldHandleEnvironment.() -> Boolean = { true },
    processRequest: suspend ProcessRequestEnvironment.() -> MessageResponse
) {
    addHandler(
        ButtonPressedHandler(
            shouldHandleBlock = shouldHandle,
            processRequestBlock = processRequest
        )
    )
}

internal class ButtonPressedHandler(
    private val shouldHandleBlock: suspend ButtonPressedShouldHandleEnvironment.() -> Boolean,
    private val processRequestBlock: suspend ProcessRequestEnvironment.() -> MessageResponse
) : Handler {

    override suspend fun shouldHandle(request: ShouldRequestEnvironment): Boolean {
        return request.message.request.type == RequestContentType.ButtonPressed &&
                shouldHandleBlock(ButtonPressedShouldHandleEnvironment(request))
    }

    override suspend fun processRequest(request: ProcessRequestEnvironment): MessageResponse {
        return processRequestBlock(request)
    }
}