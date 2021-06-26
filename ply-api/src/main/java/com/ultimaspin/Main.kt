package com.ultimaspin

import com.ultimaspin.dao.*
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.*
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.jackson.jackson
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jdbi.v3.core.Jdbi
import java.io.File

fun main() {

    // todo highlight overall match score in fixture view with winner colour
    // todo highlight home and away team in table headers to match the games as well

    // todo bring some life into the leagues
    // todo players can follow their connections
    // todo players can comment on games etc.
    // todo make the league experience more social online

    val jdbi = Jdbi.create("jdbc:postgresql://localhost/ply?user=ply&password=docker")
    val fixtureDao = FixtureDao(jdbi)
    val playerDao = PlayerDao(jdbi)
    val fixtureRepo = FixtureRepo(fixtureDao)

    val leagueDao = LeagueDao(jdbi)
    val leagueRepo = LeagueRepo(leagueDao)

    val server = embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            jackson {
                // todo do I need anything here?
            }
        }
        install(FreeMarker) {
            // todo use a different class loader please!
            templateLoader = ClassTemplateLoader(App::class.java.classLoader, "templates")
        }
        routing {

            static("/static") {
                // todo this static root currently relies on intellij setting the working directory so what do I do for production???
                staticRootFolder = File("src/main/resources")
                files("templates")

            }

            get("/") {
                call.respondText("Hello, world!", ContentType.Text.Html)
            }

            get("/league/{leagueId}") {
                val leagueId = call.parameters["leagueId"]!!
                val league = leagueRepo.getLeague(leagueId.toInt())
                call.respond(FreeMarkerContent("league.ftl", mapOf("league" to league)))
            }

            get("/fixture/{fixtureId}") {
                val fixtureId = call.parameters["fixtureId"]!!
                val fixture = fixtureRepo.getFixture(fixtureId.toInt())
                call.respond(FreeMarkerContent("scorecard.ftl", mapOf("fixture" to fixture)))
            }

            get("/division/{divisionId}") {
                val divisionId = call.parameters["divisionId"]!!
                val teams = leagueDao.getTeams(divisionId.toInt())
                call.respond(FreeMarkerContent("division.ftl", mapOf("teams" to teams)))
            }

            route("/api") {
                route("player") {
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
                    call.respond(fixtureRepo.getFixture(fixtureId.toInt()))
                }
            }

        }
    }
    server.start(wait = true)
}

