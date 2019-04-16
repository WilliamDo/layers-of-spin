package com.ultimaspin.model

import org.junit.Ignore
import org.junit.Test
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertSame

class TableTennisTest {

    @Ignore
    @Test
    fun foo() {
        val match = Match(Player("Joe"), Player("Fred"),5)
        assertEquals("Joe", match.player1.name)
        assertEquals("Fred", match.player2.name)
    }

    @Test
    fun `player 1 wins with a score of 11`() {
        val player1 = Player("Joe")
        val player2 = Player("Fred")
        val game = Game(
                player1 = player1,
                player2 = player2,
                pointsForPlayer1 = 11,
                pointsForPlayer2 = 5
        )

        assertSame(player1, game.getWinner())

    }

    @Test
    fun `player 1 wins game with a score over 11`() {
        val player1 = Player("Joe")
        val player2 = Player("Fred")
        val game = Game(
                player1 = player1,
                player2 = player2,
                pointsForPlayer1 = 14,
                pointsForPlayer2 = 12
        )

        assertSame(player1, game.getWinner())
    }

    @Test
    fun `player 2 wins game with a score of 11`() {
        val player1 = Player("Joe")
        val player2 = Player("Fred")
        val game = Game(
                player1 = player1,
                player2 = player2,
                pointsForPlayer1 = 5,
                pointsForPlayer2 = 11
        )

        assertSame(player2, game.getWinner())
    }

    @Test
    fun `player 2 wins game with a score over 11`() {
        val player1 = Player("Joe")
        val player2 = Player("Fred")
        val game = Game(
                player1 = player1,
                player2 = player2,
                pointsForPlayer1 = 12,
                pointsForPlayer2 = 14
        )

        assertSame(player2, game.getWinner())
    }

    // todo consider using assertj to make this test more readable
    @Test(expected = IllegalStateException::class)
    fun `invalid game state throws exception`() {
        val player1 = Player("Joe")
        val player2 = Player("Fred")
        val game = Game(
                player1 = player1,
                player2 = player2,
                pointsForPlayer1 = 0,
                pointsForPlayer2 = 0
        )

    }

}