package me.hellokaton.ktor.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(val username: String, val password: String)

fun Route.login() {

    post("/login") {
        val user = call.receive<User>()

        val env = call.application.environment
        val secret = env.config.property("jwt.secret").getString()
        val issuer = env.config.property("jwt.issuer").getString()
        val audience = env.config.property("jwt.audience").getString()

        // Check username and password
        // ...
        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(secret))
        call.respond(hashMapOf("token" to token))
    }
}

fun Route.hello() {
    authenticate("auth-jwt") {
        get("/hello") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
        }
    }
}

fun Application.registerAuthRoutes() {
    routing {
        login()
        hello()
    }
}
