package com.ultimaspin.model

import java.lang.IllegalStateException

class Player(val name: String)

class Match(val player1: Player,
            val player2: Player,
            numberOfGames: Int) {


    fun getWinner(): Player {
        TODO()
    }

    fun getLoser(): Player {
        TODO()
    }

    fun countGamesForPlayer1(): Int {
        TODO()
    }

    fun countGamesForPlayer2(): Int {
        TODO()
    }

}

class Game(val player1: Player,
           val pointsForPlayer1: Int,
           val player2: Player,
           val pointsForPlayer2: Int,
           private val pointsToWin: Int = DEFAULT_POINTS_TO_WIN) {

    init {
        if (!isPlayer1TheWinner() && !isPlayer2TheWinner()) {
            throw IllegalStateException("Invalid game state")
        }
    }


    fun getWinner(): Player {

        if (isPlayer1TheWinner()) {
            return player1
        } else if (isPlayer2TheWinner()) {
            return player2
        }

        throw IllegalStateException("Cannot determine winner of this game")

    }

    private fun isPlayer1TheWinner(): Boolean {
        val player1WinsCleanGame = pointsForPlayer1 == pointsToWin && pointsForPlayer2 <= pointsToWin - 2
        val player1WinsAfterDeuce = pointsForPlayer1 > pointsToWin && pointsForPlayer1 - pointsForPlayer2 >= 2
        return player1WinsCleanGame || player1WinsAfterDeuce
    }

    private fun isPlayer2TheWinner(): Boolean {
        val player2WinsCleanGame = pointsForPlayer2 == pointsToWin && pointsForPlayer1 <= pointsToWin - 2
        val player2WinsAfterDeuce = pointsForPlayer2 > pointsToWin && pointsForPlayer2 - pointsForPlayer1 >= 2
        return player2WinsCleanGame || player2WinsAfterDeuce
    }


    fun getLoser(): Player {
        TODO()
    }

    companion object {
        const val DEFAULT_POINTS_TO_WIN: Int = 11
    }

}