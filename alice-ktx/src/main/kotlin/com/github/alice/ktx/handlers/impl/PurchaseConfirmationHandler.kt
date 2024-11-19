package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.handlers.environments.ShouldRequestEnvironment
import com.github.alice.ktx.models.request.content.RequestContentType
import com.github.alice.ktx.models.response.MessageResponse

@AliceDsl
data class PurchaseConfirmationShouldRequestEnvironment(
    private val request: ShouldRequestEnvironment
) : ShouldRequestEnvironment by request {

    val purchaseRequestId = message.request.purchaseRequestId
    val purchaseToken = message.request.purchaseToken
    val orderId = message.request.orderId
    val purchaseTimestamp = message.request.purchaseTimestamp
    val purchasePayload = message.request.purchasePayload
    val signedData = message.request.signedData
    val signature = message.request.signature
}

@AliceDsl
data class PurchaseConfirmationProcessRequestEnvironment(
    private val request: ProcessRequestEnvironment
) : ProcessRequestEnvironment by request {

    val purchaseRequestId = message.request.purchaseRequestId
    val purchaseToken = message.request.purchaseToken
    val orderId = message.request.orderId
    val purchaseTimestamp = message.request.purchaseTimestamp
    val purchasePayload = message.request.purchasePayload
    val signedData = message.request.signedData
    val signature = message.request.signature
}

@AliceDsl
fun Dispatcher.purchaseConfirmation(
    shouldHandle: suspend PurchaseConfirmationShouldRequestEnvironment.() -> Boolean = { true },
    processRequest: suspend PurchaseConfirmationProcessRequestEnvironment.() -> MessageResponse
) {
    addHandler(
        PurchaseConfirmationHandler(
            shouldHandleBlock = shouldHandle,
            processRequestBlock = processRequest
        )
    )
}

internal class PurchaseConfirmationHandler(
    private val shouldHandleBlock: suspend PurchaseConfirmationShouldRequestEnvironment.() -> Boolean,
    private val processRequestBlock: suspend PurchaseConfirmationProcessRequestEnvironment.() -> MessageResponse
): Handler {
    override suspend fun shouldHandle(request: ShouldRequestEnvironment): Boolean {
        return request.message.request.type == RequestContentType.PurchaseConfirmation
                && shouldHandleBlock(PurchaseConfirmationShouldRequestEnvironment(request))
    }

    override suspend fun processRequest(request: ProcessRequestEnvironment): MessageResponse {
        return processRequestBlock(PurchaseConfirmationProcessRequestEnvironment(request))
    }
}