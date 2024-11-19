package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.handlers.environments.ShouldRequestEnvironment
import com.github.alice.ktx.models.request.content.RequestContentType
import com.github.alice.ktx.models.response.MessageResponse

@AliceDsl
data class ShowPullShouldRequestEnvironment(
    private val request: ShouldRequestEnvironment
) : ShouldRequestEnvironment by request {

    val showType = checkNotNull(request.message.request.showType)
}

@AliceDsl
data class ShowPullProcessRequestEnvironment(
    private val request: ProcessRequestEnvironment
) : ProcessRequestEnvironment by request {

    val showType = checkNotNull(request.message.request.showType)
}

@AliceDsl
fun Dispatcher.showPull(
    shouldHandle: suspend ShowPullShouldRequestEnvironment.() -> Boolean = { true },
    processRequest: suspend ShowPullProcessRequestEnvironment.() -> MessageResponse
) {
    addHandler(
        ShowPullHandler(
            shouldHandleBlock = shouldHandle,
            processRequestBlock = processRequest
        )
    )
}

internal class ShowPullHandler(
    private val shouldHandleBlock: suspend ShowPullShouldRequestEnvironment.() -> Boolean,
    private val processRequestBlock: suspend ShowPullProcessRequestEnvironment.() -> MessageResponse
) : Handler {
    override suspend fun shouldHandle(request: ShouldRequestEnvironment): Boolean {
        return request.message.request.type == RequestContentType.ShowPull
                && shouldHandleBlock(ShowPullShouldRequestEnvironment(request))
    }

    override suspend fun processRequest(request: ProcessRequestEnvironment): MessageResponse {
        return processRequestBlock(ShowPullProcessRequestEnvironment(request))
    }
}