package me.hellokaton.ktor

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.cio.websocket.*
import io.ktor.serialization.*
import io.ktor.websocket.*
import me.hellokaton.ktor.routes.registerChatRoutes
import me.hellokaton.ktor.routes.registerCustomerRoutes
import me.hellokaton.ktor.routes.registerOrderRoutes
import java.time.Duration

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        json()
    }
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    registerCustomerRoutes()
    registerOrderRoutes()
    registerChatRoutes()
}