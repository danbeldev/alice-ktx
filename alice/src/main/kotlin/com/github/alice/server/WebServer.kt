package com.github.alice.server

interface WebServer {

    fun run(callback: WebServerResponseCallback)

}