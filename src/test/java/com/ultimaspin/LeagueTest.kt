package com.ultimaspin

import com.ultimaspin.dao.LeagueDao
import org.jdbi.v3.core.Jdbi
import org.junit.Test

class LeagueTest {

    @Test
    fun getLeague() {
        val jdbi = Jdbi.create("jdbc:postgresql://localhost/ply?user=ply&password=docker")
        val dao = LeagueDao(jdbi)

        val league = dao.getLeague(1)
        println(league)

        val seasons = dao.getSeasons(league.id)
        println(seasons)

        val divisions = dao.getDivisions(seasons[0].id)
        println(divisions)

    }

}