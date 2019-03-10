package com.ultimaspin.dao

import org.jdbi.v3.core.Jdbi
import java.time.LocalDate

class LeagueDao(private val jdbi: Jdbi) {

    fun getLeague(leagueId: Int): League {
        return jdbi.withHandle<League, Exception> {
            it.createQuery("select * from league where id = :leagueId")
                    .bind("leagueId", leagueId)
                    .map { rs, _ -> League(rs.getInt("id"), rs.getString("league_name")) }
                    .findOnly()
        }
    }

    fun getSeasons(leagueId: Int): List<Season> {
        return jdbi.withHandle<List<Season>, Exception> {
            it.createQuery("select * from season where league_id = :leagueId")
                    .bind("leagueId", leagueId)
                    .map { rs, _ -> Season(rs.getInt("id"), rs.getDate("start_date").toLocalDate()) }
                    .toList()
        }
    }

    fun getDivisions(seasonId: Int): List<Division> {
        return jdbi.withHandle<List<Division>, Exception> {
            it.createQuery("select * from division where season_id = :seasonId")
                    .bind("seasonId", seasonId)
                    .map { rs, _ -> Division(rs.getInt("id"), rs.getInt("division")) }
                    .toList()
        }
    }

}

class LeagueRepo(private val leagueDao: LeagueDao) {

    fun getLeague(leagueId: Int): LeagueResponse {
        val league = leagueDao.getLeague(leagueId)
        val seasons = leagueDao.getSeasons(leagueId)
        val currentSeason = seasons.sortedBy { it.startDate }.last()
        val currentDivisions = leagueDao.getDivisions(currentSeason.id)

        return LeagueResponse(
                name = league.name,
                seasons = seasons,
                currentSeason = currentSeason,
                currentDivisions = currentDivisions
        )
    }

}

data class LeagueResponse(val name: String,
                          val seasons: List<Season>,
                          val currentSeason: Season,
                          val currentDivisions: List<Division>)

data class League(val id: Int, val name: String)

data class Season(val id: Int, val startDate: LocalDate)

data class Division(val id: Int, val division: Int)