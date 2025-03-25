package com.github.alice.ktx.testutils.builders

import com.github.alice.ktx.api.dialog.DialogApi
import com.github.alice.ktx.fsm.MutableFSMContext
import com.github.alice.ktx.fsm.models.FSMStrategy
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.testutils.mothers.MessageRequestMother
import io.mockk.mockk

class ProcessRequestEnvironmentBuilder {
    var message: MessageRequest = MessageRequestMother.createMessageRequest()
    var context: MutableFSMContext = mockk(relaxed = true)
    var dialogApi: DialogApi? = mockk(relaxed = true)

    var fsmStrategy: FSMStrategy = mockk(relaxed = true)
    var enableApiStorage: Boolean = false

    fun build(): ProcessRequestEnvironment = object : ProcessRequestEnvironment {
        override val message: MessageRequest = this@ProcessRequestEnvironmentBuilder.message
        override val context: MutableFSMContext = this@ProcessRequestEnvironmentBuilder.context
        override val dialogApi: DialogApi? = this@ProcessRequestEnvironmentBuilder.dialogApi
        override val fsmStrategy: FSMStrategy = this@ProcessRequestEnvironmentBuilder.fsmStrategy
        override val enableApiStorage: Boolean = this@ProcessRequestEnvironmentBuilder.enableApiStorage
    }

    companion object {
        fun processRequestEnvironment(block: ProcessRequestEnvironmentBuilder.() -> Unit = {}): ProcessRequestEnvironment {
            return ProcessRequestEnvironmentBuilder().apply(block).build()
        }
    }
}