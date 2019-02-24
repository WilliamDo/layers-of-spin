package com.ultimaspin.dao

import com.ultimaspin.Player
import org.jdbi.v3.core.Jdbi

class PlayerDao(private val jdbi: Jdbi) {

    fun getPlayer(playerId: Int): Player {
        return jdbi.withHandle<Player, Exception> {
            it.createQuery("select * from player where id = :playerId")
                    .bind("playerId", playerId)
                    .map { rs, _ -> Player(rs.getString("first_name"), rs.getString("last_name")) }
                    .findOnly()
        }
    }

    fun createPlayer(firstName: String, lastName: String): Int {
        return jdbi.withHandle<Int, Exception> {
            it.createQuery("INSERT INTO player (first_name, last_name) VALUES (:firstName, :lastName) RETURNING id")
                    .bind("firstName", firstName)
                    .bind("lastName", lastName)
                    .mapTo(Int::class.java)
                    .findOnly()
        }
    }

}