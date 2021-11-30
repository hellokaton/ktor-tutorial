package me.hellokaton.ktor.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Response(val code: Int, val message: String?, @Contextual val data: Any?) {

    companion object {
        fun success(): Response {
            return Response(0, null, null)
        }

        fun success(data: Any): Response {
            return Response(0, null, data)
        }

        fun success(message: String): Response {
            return Response(0, message, null)
        }

        fun fail(message: String): Response {
            return Response(-1, message, null)
        }

        fun success(message: String, data: Any): Response {
            return Response(0, message, data)
        }
    }

}
