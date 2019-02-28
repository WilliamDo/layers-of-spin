package com.ultimaspin.dao

import org.jdbi.v3.core.Jdbi

class FixtureRepo(private val fixtureDao: FixtureDao) {
    fun getFixture(fixtureId: Int): FixtureResponse {

        val fixtureDetails = fixtureDao.getFixtureDetails(fixtureId)
        val matches = fixtureDao.getMatches(fixtureId) // todo this can be run in parallel with the fixture query

        val homePlayers = fixtureDao.getFixturePlayers(fixtureDetails.homeTeamId)
        val awayPlayers = fixtureDao.getFixturePlayers(fixtureDetails.awayTeamId)

        return FixtureResponse(
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
                        FixtureDetails(
                                rs.getInt("home_team_id"),
                                rs.getInt("away_team_id"),
                                rs.getString("home_team"),
                                rs.getString("away_team")
                        )
                    }.findOnly()
        }

    }

    fun getFixturePlayers(fixtureTeamId: Int): List<Player> {
        // todo make this a constant
        val sql = """
            select *
            from fixture_player fp
            inner join player p on fp.player_id = p.id
            where fp.fixture_team_id = :fixtureTeamId
        """.trimIndent()

        return jdbi.withHandle<List<Player>, Exception> { handle ->
            handle.createQuery(sql)
                    .bind("fixtureTeamId", fixtureTeamId)
                    .map { rs, _ -> Player(rs.getString("first_name"), rs.getString("last_name")) }
                    .toList()
        }
    }

    fun getMatches(fixtureId: Int): List<Match> {
        val sql = "select * from fixture_match where fixture_id = :fixtureId"

        return jdbi.withHandle<List<Match>, Exception> { handle ->
            handle.createQuery(sql)
                    .bind("fixtureId", fixtureId)
                    // todo turn this into JSON?
                    .map { rs, _ -> Match(homeScore = rs.getString("home_score"), awayScore = rs.getString("away_score")) }
                    .toList()

        }
    }

}

data class Match(val homeScore: String, val awayScore: String)

data class Player(val firstName: String, val lastName: String)

data class Team(val name: String, val players: List<Player>)

data class FixtureDetails(val homeTeamId: Int,
                          val awayTeamId: Int,
                          val homeTeam: String,
                          val awayTeam: String)

data class FixtureResponse(val homeTeam: Team, val awayTeam: Team, val matches: List<Match>)