package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.handlers.environments.ShouldRequestEnvironment
import com.github.alice.ktx.models.request.content.RequestContentType
import com.github.alice.ktx.models.response.MessageResponse

@AliceDsl
data class AudioPlayerShouldRequestEnvironment(
    private val request: ShouldRequestEnvironment
) : ShouldRequestEnvironment by request {

    val type = message.request.type
    val error = message.request.error
}

@AliceDsl
data class AudioPlayerProcessRequestEnvironment(
    private val request: ProcessRequestEnvironment
) : ProcessRequestEnvironment by request {

    val type = message.request.type
    val error = message.request.error
}

@AliceDsl
fun Dispatcher.audioPlayer(
    shouldHandle: suspend AudioPlayerShouldRequestEnvironment.() -> Boolean = { true },
    processRequest: suspend AudioPlayerProcessRequestEnvironment.() -> MessageResponse
) {
    addHandler(
        AudioPlayerHandler(
            shouldHandleBlock = shouldHandle,
            processRequestBlock = processRequest
        )
    )
}

internal class AudioPlayerHandler (
    private val shouldHandleBlock: suspend AudioPlayerShouldRequestEnvironment.() -> Boolean,
    private val processRequestBlock: suspend AudioPlayerProcessRequestEnvironment.() -> MessageResponse
): Handler {
    override suspend fun shouldHandle(request: ShouldRequestEnvironment): Boolean {
        val audioPlayerTypes = setOf(
            RequestContentType.AudioPlayerPlaybackStarted,
            RequestContentType.AudioPlayerPlaybackFinished,
            RequestContentType.AudioPlayerPlaybackNearlyFinished,
            RequestContentType.AudioPlayerPlaybackStopped,
            RequestContentType.AudioPlayerPlaybackFailed,
        )

        return request.message.request.type in audioPlayerTypes && shouldHandleBlock(AudioPlayerShouldRequestEnvironment(request))
    }

    override suspend fun processRequest(request: ProcessRequestEnvironment): MessageResponse {
        return processRequestBlock(AudioPlayerProcessRequestEnvironment(request))
    }
}