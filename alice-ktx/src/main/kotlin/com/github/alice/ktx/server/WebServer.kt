package com.github.alice.ktx.server

interface WebServer {

    fun run(callback: WebServerResponseCallback)

}