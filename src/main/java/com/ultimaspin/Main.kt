package com.ultimaspin

import com.ultimaspin.dao.PlayerDao
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.*
import io.ktor.jackson.jackson
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jdbi.v3.core.Jdbi

fun main() {

    val jdbi = Jdbi.create("jdbc:postgresql://localhost/ply?user=ply&password=docker")
    val fixtureDao = FixtureDao(jdbi)
    val playerDao = PlayerDao(jdbi)
    val repo = FixtureRepo(fixtureDao)

    val server = embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            jackson {
            }
        }
        routing {
            get("/") {
                call.respondText("Hello, world!", ContentType.Text.Html)
            }
            route("/player") {
                get("{playerId}") {
                    val playerId = call.parameters["playerId"]!!
                    call.respond(playerDao.getPlayer(playerId.toInt()))
                }
                post {
                    // todo get name from request body
                    call.respond(mapOf("id" to playerDao.createPlayer("Joe", "Bloggs")))
                }
            }
            get("/fixture/{fixtureId}") {
                val fixtureId = call.parameters["fixtureId"]!!
                call.respond(repo.getFixture(fixtureId.toInt()))
            }
        }
    }
    server.start(wait = true)
}

