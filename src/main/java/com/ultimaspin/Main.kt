package com.ultimaspin

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.*
import io.ktor.jackson.jackson
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jdbi.v3.core.Jdbi

fun main(args: Array<String>) {

    val jdbi = Jdbi.create("jdbc:postgresql://localhost/ply?user=ply&password=docker")
    val dao = Dao(jdbi)
    val repo = FixtureRepo(dao)

    val server = embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            jackson {
            }
        }
        routing {
            get("/") {
                call.respondText("Hello, world!", ContentType.Text.Html)
            }
            get("/snippets") {
                call.respond(mapOf("OK" to true))
            }
            get("/fixture") {
                call.respond(repo.getFixture(1))
            }
        }
    }
    server.start(wait = true)
}

