package com.ultimaspin

import org.jdbi.v3.core.Jdbi

fun main() {
    val jdbi = Jdbi.create("jdbc:postgresql://localhost/ply?user=ply&password=docker")

    val generator = TestDataGenerator(jdbi)

    generator.createLeague("Simon's League")
    generator.createLeague("Spinners")
    generator.createLeague("Rubber Glue")
}

class TestDataGenerator(private val jdbi: Jdbi) {

    // todo extract this into DAO classes for code re-use!!!

    fun createLeague(name: String) {

        val leagueId: Int = jdbi.withHandle<Int, Exception> { handle ->
            handle.createQuery("insert into league (league_name) values (:leagueName) returning id")
                    .bind("leagueName", name)
                    .mapTo(Int::class.java)
                    .findOnly()
        }


        val seasonId: Int = jdbi.withHandle<Int, Exception> { handle ->
            handle.createQuery("insert into season (league_id, start_date) values (:leagueId, '2018-09-01') returning id")
                    .bind("leagueId", leagueId)
                    .mapTo(Int::class.java)
                    .findOnly()
        }

        val divisionId: Int = jdbi.withHandle<Int, Exception> { handle ->
            handle.createQuery("insert into division (season_id, division) values (:seasonId, 1) returning id")
                    .bind("seasonId", seasonId)
                    .mapTo(Int::class.java)
                    .findOnly()
        }


        val teamIds = (1..10).map {
            jdbi.withHandle<Int, Exception> { handle ->
                handle.createQuery("insert into team (team_name) values (:teamName) returning id")
                        .bind("teamName", "Team $name $it")
                        .mapTo(Int::class.java)
                        .findOnly()
            }
        }

        // todo create fixture set

        createFixtures(divisionId, teamIds)


    }

    fun createFixtures(divisionId: Int, teamIds: List<Int>) {

        val mappings = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until teamIds.size) {
            for (j in i + 1 until teamIds.size) {
                println("${teamIds[i]}, ${teamIds[j]}")
                mappings.add(teamIds[i] to teamIds[j])
            }
        }

        mappings.forEach { (team1, team2) ->

            val homeId = jdbi.withHandle<Int, Exception> { handle ->
                handle.createQuery("INSERT INTO fixture_team (team_id) VALUES (:teamId) RETURNING id")
                        .bind("teamId", team1)
                        .mapTo(Int::class.java)
                        .findOnly()
            }

            val awayId = jdbi.withHandle<Int, Exception> { handle ->
                handle.createQuery("INSERT INTO fixture_team (team_id) VALUES (:teamId) RETURNING id")
                        .bind("teamId", team2)
                        .mapTo(Int::class.java)
                        .findOnly()
            }


            jdbi.withHandle<Int, Exception> { handle ->
                handle.createQuery("""
                INSERT INTO fixture (division_id, fixture_date, home_team_id, away_team_id)
                VALUES (:divisionId, '2018-12-19', :homeTeamId, :awayTeamId)
                RETURNING id
                """.trimIndent())
                        .bind("divisionId", divisionId)
                        .bind("homeTeamId", homeId)
                        .bind("awayTeamId", awayId)
                        .mapTo(Int::class.java)
                        .findOnly()
            }

            // todo home leg as well?
            // todo do a batch insert since we want to reuse the same prepared statement

        }


    }
}

