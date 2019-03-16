package com.ultimaspin.dao

import com.google.gson.Gson
import org.jdbi.v3.core.Jdbi
import java.sql.ResultSet
import java.time.LocalDate

class FixtureRepo(private val fixtureDao: FixtureDao) {
    fun getFixture(fixtureId: Int): FixtureResponse {

        val fixtureDetails = fixtureDao.getFixtureDetails(fixtureId)
        val matches = fixtureDao.getMatches(fixtureId) // todo this can be run in parallel with the fixture query

        val homePlayers = fixtureDao.getFixturePlayers(fixtureDetails.homeTeamId)
        val awayPlayers = fixtureDao.getFixturePlayers(fixtureDetails.awayTeamId)

        return FixtureResponse(
                date = fixtureDetails.date,
                homeTeam = Team(fixtureDetails.homeTeam, homePlayers),
                awayTeam = Team(fixtureDetails.awayTeam, awayPlayers),
                matches = matches
        )
    }
}

class FixtureDao(private val jdbi: Jdbi) {


    fun getFixtureDetails(fixtureId: Int): FixtureDetails {

        // todo make this a constant
        val sql = """
            select f.fixture_date, f.home_team_id, f.away_team_id, th.team_name home_team, ta.team_name away_team
            from fixture f
            inner join fixture_team fth on fth.id = f.home_team_id
            inner join fixture_team fta on fta.id = f.away_team_id
            inner join team th on fth.team_id = th.id
            inner join team ta on fta.team_id = ta.id
            where f.id = :fixtureId;
        """.trimIndent()

        return jdbi.withHandle<FixtureDetails, Exception> { handle ->
            handle.createQuery(sql)
                    .bind("fixtureId", fixtureId)
                    .map { rs, _ ->
                        // todo parse the date of the fixture
                        FixtureDetails(
                                rs.getDate("fixture_date").toLocalDate(),
                                rs.getInt("home_team_id"),
                                rs.getInt("away_team_id"),
                                rs.getString("home_team"),
                                rs.getString("away_team")
                        )
                    }.findOnly()
        }

    }

    fun getFixturePlayers(fixtureTeamId: Int): List<FixturePlayer> {
        // todo make this a constant
        val sql = """
            select *
            from fixture_player fp
            inner join player p on fp.player_id = p.id
            where fp.fixture_team_id = :fixtureTeamId
        """.trimIndent()

        return jdbi.withHandle<List<FixturePlayer>, Exception> { handle ->
            handle.createQuery(sql)
                    .bind("fixtureTeamId", fixtureTeamId)
                    .map { rs, _ ->
                        FixturePlayer(
                                fixturePlayerId = rs.getInt("id"),
                                firstName = rs.getString("first_name"),
                                lastName = rs.getString("last_name"))
                    }
                    .toList()
        }
    }

    fun getMatches(fixtureId: Int): List<Match> {
        val sql = "select * from fixture_match where fixture_id = :fixtureId"

        return jdbi.withHandle<List<Match>, Exception> { handle ->
            handle.createQuery(sql)
                    .bind("fixtureId", fixtureId)
                    .map { rs, _ ->
                        Match(
                                homePlayerId = rs.getInt("home_player_id"),
                                awayPlayerId = rs.getInt("away_player_id"),
                                homeScore = rs.getScore("home_score"),
                                awayScore = rs.getScore("away_score")
                        )
                    }
                    .toList()

        }
    }

    private fun ResultSet.getScore(fieldName: String): Array<Int> {
        val characterStream = getCharacterStream(fieldName)
        // todo maybe use built-in json from ktor instead of using an extra dependency?
        val gson = Gson()
        return gson.fromJson(characterStream, Array<Int>::class.java)
    }

}

data class Match(val homePlayerId: Int,
                 val awayPlayerId: Int,
                 val homeScore: Array<Int>,
                 val awayScore: Array<Int>) {

    // todo validate that away and home score arrays are same size or enforce it with a type
    val numberOfGames = homeScore.size

    fun winnerPlayerId(): Int? {

        var homeGames = 0;
        var awayGames = 0;
        for (i in 0 until homeScore.size) {
            if (homeScore[i] > awayScore[i]) {
                homeGames++
            } else if (awayScore[i] > homeScore[i]) {
                awayGames++
            }
        }

        return when {
            homeGames > awayGames -> homePlayerId
            awayGames > homeGames -> awayPlayerId
            else -> null
        }

    }
}

data class FixturePlayer(val fixturePlayerId: Int, val firstName: String, val lastName: String)

data class Team(val name: String, val players: List<FixturePlayer>) {
    fun getPlayer(id: Int): FixturePlayer {
        // todo find a better way to protect myself from this forcing the optional
        return players.find { it.fixturePlayerId == id }!!
    }
}

data class FixtureDetails(val date: LocalDate,
                          val homeTeamId: Int,
                          val awayTeamId: Int,
                          val homeTeam: String,
                          val awayTeam: String)

data class FixtureResponse(val date: LocalDate, val homeTeam: Team, val awayTeam: Team, val matches: List<Match>) {

    val homeScore = matches.filter {
        homeTeam.players.map { p ->
            p.fixturePlayerId
        }.contains(it.winnerPlayerId())
    }.size

    val awayScore = matches.filter {
        awayTeam.players.map { p ->
            p.fixturePlayerId
        }.contains(it.winnerPlayerId())
    }.size

}