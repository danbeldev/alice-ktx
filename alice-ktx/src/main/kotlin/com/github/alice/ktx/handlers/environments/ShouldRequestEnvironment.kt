package com.github.alice.ktx.handlers.environments

import com.github.alice.ktx.api.dialog.DialogApi
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.fsm.ReadOnlyFSMContext
import com.github.alice.ktx.handlers.filters.Filter
import com.github.alice.ktx.models.request.MessageRequest

@AliceDsl
interface ShouldRequestEnvironment {
    val message: MessageRequest
    val context: ReadOnlyFSMContext
    val dialogApi: DialogApi?

    fun filter(filter: Filter): Boolean {
        return filter.checkFor(this)
    }
}