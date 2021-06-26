package com.ultimaspin

import com.ultimaspin.dao.FixtureDao
import org.jdbi.v3.core.Jdbi
import org.junit.Test

class FixtureTest {


    @Test
    fun getMatches() {
        val jdbi = Jdbi.create("jdbc:postgresql://localhost/ply?user=ply&password=docker")
        val dao = FixtureDao(jdbi)
        val matches = dao.getMatches(1)

        println(matches)

    }

}