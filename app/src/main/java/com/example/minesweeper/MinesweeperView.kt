package com.example.minesweeper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class MinesweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    // needs a paint object to draw on canvas
    private lateinit var paintBackground: Paint
    private lateinit var paintLine: Paint
    private lateinit var paintText: Paint

    init {
        paintBackground = Paint()
        paintBackground.color = Color.GRAY
        paintBackground.style = Paint.Style.FILL

        paintLine = Paint()
        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f

        paintText = Paint()
        paintText.color = Color.GREEN

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)

        canvas?.drawText("test1", (0 * width / 5).toFloat(), (1 * height / 5).toFloat(), paintText)

        drawGameArea(canvas!!)
        drawModel(canvas!!)
    }

    /*
    * SETUP:
    * Draws outline of the board: 4 vertical and 4 horizontal lines
    * */
    private fun drawGameArea(canvas: Canvas) {
        // border
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
        // 4 horizontal lines
        canvas.drawLine(0f, (height / 5).toFloat(), width.toFloat(), (height / 5).toFloat(), paintLine)
        canvas.drawLine(0f, (2 * height / 5).toFloat(), width.toFloat(), (2 * height / 5).toFloat(), paintLine)
        canvas.drawLine(0f, (3 * height / 5).toFloat(), width.toFloat(), (3 * height / 5).toFloat(), paintLine)
        canvas.drawLine(0f, (4 * height / 5).toFloat(), width.toFloat(), (4 * height / 5).toFloat(), paintLine)

        // 4 vertical lines
        canvas.drawLine((width / 5).toFloat(), 0f, (width / 5).toFloat(), height.toFloat(), paintLine)
        canvas.drawLine((2 * width / 5).toFloat(), 0f, (2 * width / 5).toFloat(), height.toFloat(), paintLine)
        canvas.drawLine((3 * width / 5).toFloat(), 0f, (3 * width / 5).toFloat(), height.toFloat(), paintLine)
        canvas.drawLine((4 * width / 5).toFloat(), 0f, (4 * width / 5).toFloat(), height.toFloat(), paintLine)
    }

    private fun drawModel(canvas: Canvas) {
        for (i in 0..4) {
            for (j in 0..4) {
                val tX = (i * width / 5).toFloat()
                val tY = (j * height / 5).toFloat()
                val content = MinesweeperModel.getFieldContent(i, j)

                when (content) {
                    MinesweeperModel.MINE -> canvas.drawText(MinesweeperModel.MINE.toString(), tX, tY, paintText)
                    MinesweeperModel.ONE -> canvas.drawText(MinesweeperModel.ONE.toString(), tX, tY, paintText)
                    MinesweeperModel.TWO -> canvas.drawText(MinesweeperModel.TWO.toString(), tX, tY, paintText)
                    MinesweeperModel.THREE -> canvas.drawText(MinesweeperModel.THREE.toString(), tX, tY, paintText)
                    MinesweeperModel.FOUR -> canvas.drawText(MinesweeperModel.FOUR.toString(), tX, tY, paintText)
                    MinesweeperModel.FIVE -> canvas.drawText(MinesweeperModel.FIVE.toString(), tX, tY, paintText)
                    MinesweeperModel.SIX -> canvas.drawText(MinesweeperModel.SIX.toString(), tX, tY, paintText)
                    MinesweeperModel.SEVEN -> canvas.drawText(MinesweeperModel.SEVEN.toString(), tX, tY, paintText)
                    MinesweeperModel.EIGHT -> canvas.drawText(MinesweeperModel.EIGHT.toString(), tX, tY, paintText)
                }
            }
        }
    }

    fun resetGame() {
        MinesweeperModel.resetBoard()
        MinesweeperModel.setMines()
        MinesweeperModel.setCellCount()
        // draws the updated board
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val tX = event.x.toInt() / (width / 5)
            val tY = event.y.toInt() / (height / 5)

            if (tX < 5 && tY < 5 && MinesweeperModel.getFieldContent(tX, tY) == MinesweeperModel.BLANK) {
                MinesweeperModel.reveal(tX, tY)
                invalidate()
            }

        }
        return true
    }



}