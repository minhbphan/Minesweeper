package com.example.minesweeper

import kotlin.random.Random

object MinesweeperModel {

    /* ***** CONSTANTS ***** */
    // possible content of cells
    private const val BLANK: Short = 0
    const val ONE: Short = 1
    const val TWO: Short = 2
    const val THREE: Short = 3
    const val MINE: Short = 9
    // interactions with the cells
    const val REVEAL : Short = 10
    const val FLAG : Short = 11
    // possible states of each cell
    const val SHOWN : Short = 12
    const val HIDDEN : Short = 13

    var interaction = REVEAL    // default interaction is to reveal cell
    private var flagRemaining: Int = 3
    var gameEnd = false

    private val field = arrayOf(
        shortArrayOf(BLANK, BLANK, BLANK, BLANK, BLANK),
        shortArrayOf(BLANK, BLANK, BLANK, BLANK, BLANK),
        shortArrayOf(BLANK, BLANK, BLANK, BLANK, BLANK),
        shortArrayOf(BLANK, BLANK, BLANK, BLANK, BLANK),
        shortArrayOf(BLANK, BLANK, BLANK, BLANK, BLANK))
    private val cover = arrayOf(
        shortArrayOf(HIDDEN, HIDDEN, HIDDEN, HIDDEN, HIDDEN),
        shortArrayOf(HIDDEN, HIDDEN, HIDDEN, HIDDEN, HIDDEN),
        shortArrayOf(HIDDEN, HIDDEN, HIDDEN, HIDDEN, HIDDEN),
        shortArrayOf(HIDDEN, HIDDEN, HIDDEN, HIDDEN, HIDDEN),
        shortArrayOf(HIDDEN, HIDDEN, HIDDEN, HIDDEN, HIDDEN))


    /* ***** SETUP FUNCTIONS: *****
       ***** resets game, planting mines, setting adjacent mines count for cells ***** */
    fun resetBoard() {
        for (i in 0..4) {
            for (j in 0..4) {
                setFieldContent(i, j, BLANK)
                setCoverContent(i, j, HIDDEN)
            }
        }
        interaction = REVEAL
        flagRemaining = 3
    }
    fun setMines() {
        var mineCount = 3
        // ensures that exactly 3 mines are planted
        while (mineCount > 0) {
            for (i in 0..4) {
                for (j in 0..4) {
                    if ((Random(System.currentTimeMillis()).nextInt(10) == 1)
                        && (mineCount > 0) && (getFieldContent(i,j) == BLANK)) {
                        setFieldContent(i, j, MINE)
                        mineCount--
                    }
                }
            }
        }

    }
    fun setCellCount() {
        for (i in 0..4) {
            for (j in 0..4) {
                if (getFieldContent(i, j) != MINE) {
                    var mineCounter = 0

                    // horizontal & vertical checks
                    if (i > 0 && getFieldContent(i-1, j) == MINE) { mineCounter++ }
                    if (j > 0 && getFieldContent(i, j-1) == MINE) { mineCounter++ }
                    if (i < 4 && getFieldContent(i+1, j) == MINE) { mineCounter++ }
                    if (j < 4 && getFieldContent(i, j+1) == MINE) { mineCounter++ }

                    // diagonal checks
                    if (i > 0 && j > 0 && getFieldContent(i-1, j-1) == MINE) { mineCounter++ }
                    if (i > 0 && j < 4 && getFieldContent(i-1, j+1) == MINE) { mineCounter++ }
                    if (i < 4 && j > 0 && getFieldContent(i+1, j-1) == MINE) { mineCounter++ }
                    if (i < 4 && j < 4 && getFieldContent(i+1, j+1) == MINE) { mineCounter++ }

                    setFieldContent(i, j, mineCounter.toShort())
                }
            }
        }
    }

    // checks the winning state of the game (all cells are either uncovered or flagged)
    fun winningUncovered() : Boolean {
        for (i in 0..4) {
            for (j in 0..4) {
                if (getCoverContent(i, j) == HIDDEN) return false
            }
        }
        return true
    }
    // uncovers the board to show the field
    fun removeCover() {
        for (i in 0..4) {
            for (j in 0..4) {
                setCoverContent(i, j, SHOWN)
            }
        }
    }

    /* ***** GETTERS & SETTERS ***** */
    fun getFieldContent(x: Int, y: Int) = field[x][y]
    private fun setFieldContent(x: Int, y: Int, content: Short) { field[x][y] = content }
    fun getCoverContent(x: Int, y: Int) = cover[x][y]
    fun setCoverContent(x: Int, y: Int, content: Short) { cover[x][y] = content }
    fun setEndState(endState: Boolean) { gameEnd = endState }
    fun getflagRemaining() = flagRemaining
    fun setflagRemaining(count: Int) {flagRemaining = count}
}