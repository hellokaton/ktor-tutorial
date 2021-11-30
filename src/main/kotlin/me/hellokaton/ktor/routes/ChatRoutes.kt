package me.hellokaton.ktor.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*
import me.hellokaton.ktor.models.*

fun Route.chat() {
    webSocket("/chat") {
        send("建立连接成功!")
        for (frame in incoming) {
            frame as? Frame.Text ?: continue
            val receivedText = frame.readText()
            send("你发送了: $receivedText")
        }
    }
}

fun Application.registerChatRoutes() {
    routing {
        chat()
    }
}
