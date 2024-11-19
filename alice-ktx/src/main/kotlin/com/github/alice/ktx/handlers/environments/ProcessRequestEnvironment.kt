package com.github.alice.ktx.handlers.environments

import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.fsm.MutableFSMContext
import com.github.alice.ktx.fsm.models.FSMStrategy

@AliceDsl
interface ProcessRequestEnvironment : ShouldRequestEnvironment {

    override val context: MutableFSMContext

    val fsmStrategy: FSMStrategy
    val enableApiStorage: Boolean
}