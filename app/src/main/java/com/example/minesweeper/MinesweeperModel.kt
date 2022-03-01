package com.example.minesweeper

import kotlin.random.Random

object MinesweeperModel {
    // TODO: Naming conventions for constants
    // possible content of cells
    val BLANK: Short = 0
    val ONE: Short = 1
    val TWO: Short = 2
    val THREE: Short = 3
    val FOUR: Short = 4
    val FIVE: Short = 5
    val SIX: Short = 6
    val SEVEN: Short = 7
    val EIGHT: Short = 8
    val MINE: Short = 9

    // interactions with the cells
    val REVEAL : Short = 10
    val FLAG : Short = 11
    private var interaction = REVEAL    // default interaction is to reveal cell

    // possible states of each cell
    val CLICKED : Short = 12
    val UNCLICKED : Short = 13


    private val field = arrayOf(
        shortArrayOf(BLANK, BLANK, BLANK, BLANK, BLANK),
        shortArrayOf(BLANK, BLANK, BLANK, BLANK, BLANK),
        shortArrayOf(BLANK, BLANK, BLANK, BLANK, BLANK),
        shortArrayOf(BLANK, BLANK, BLANK, BLANK, BLANK),
        shortArrayOf(BLANK, BLANK, BLANK, BLANK, BLANK))

    private val cover = arrayOf(
        shortArrayOf(UNCLICKED, UNCLICKED, UNCLICKED, UNCLICKED, UNCLICKED),
        shortArrayOf(UNCLICKED, UNCLICKED, UNCLICKED, UNCLICKED, UNCLICKED),
        shortArrayOf(UNCLICKED, UNCLICKED, UNCLICKED, UNCLICKED, UNCLICKED),
        shortArrayOf(UNCLICKED, UNCLICKED, UNCLICKED, UNCLICKED, UNCLICKED),
        shortArrayOf(UNCLICKED, UNCLICKED, UNCLICKED, UNCLICKED, UNCLICKED))

    // resets the game
    fun resetBoard() {
        for (i in 0..4) {
            for (j in 0..4) {
                setFieldContent(i, j, BLANK)
                setCoverContent(i, j, UNCLICKED)
            }
        }
        interaction = REVEAL
    }

    /*
    * SETUP:
    * Plant mines
    * */
    fun setMines() {
        var mineCount = 3
        for (i in 0..4) {
            for (j in 0..4) {
                if ((Random(System.currentTimeMillis()).nextInt(2) == 1)
                    && (mineCount > 0)) {
                    setFieldContent(i, j, MINE)
                    mineCount--
                }
            }
        }
    }

    // Sets the count of surrounding mines for each cell
    fun setCellCount() {
        for (i in 0..4) {
            for (j in 0..4) {
                if (getFieldContent(i, j) != MINE) {
                    var mineCounter = 0

                    // horizontal & vertical checks
                    if (i > 0 && getFieldContent(i-1,j) == MINE) { mineCounter++ }

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



    fun reveal(x: Int, y: Int) {
        interaction = REVEAL
        setCoverContent(x, y, interaction)
    }
    fun flag(x: Int, y: Int) {
        interaction = FLAG
        setCoverContent(x, y, interaction)
    }

    /*
    * GETTERS + SETTERS
    * */
    fun getFieldContent(x: Int, y: Int) = field[x][y]
    fun setFieldContent(x: Int, y: Int, content: Short) { field[x][y] = content }
    fun getCoverContent(x: Int, y: Int) = cover[x][y]
    fun setCoverContent(x: Int, y: Int, content: Short) { cover[x][y] = content }



}