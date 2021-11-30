package me.hellokaton.ktor.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*
import me.hellokaton.ktor.chat.server.Connection
import me.hellokaton.ktor.models.*
import java.util.*
import kotlin.collections.LinkedHashSet

fun Route.chat() {
    val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())

    webSocket("/chat") {
        println("有新的连接!")
        val thisConnection = Connection(this)
        connections += thisConnection
        try {
            send("你已建立连接! 你是第 ${connections.count()} 个用户.")
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                val textWithUsername = "[${thisConnection.name}]: $receivedText"
                connections.forEach {
                    it.session.send(textWithUsername)
                }
            }
        } catch (e: Exception) {
            println(e.localizedMessage)
        } finally {
            println("断开连接: $thisConnection!")
            connections -= thisConnection
        }
    }
}

fun Application.registerChatRoutes() {
    routing {
        chat()
    }
}
