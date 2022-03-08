package com.example.minesweeper

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.google.android.material.snackbar.Snackbar

class MinesweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    // needs a paint object to draw on canvas
    private var paintBackground: Paint
    private var paintCover: Paint
    private var paintEmpty: Paint
    private var paintLine: Paint
    private var paintText: Paint
    private var bitmapMine: Bitmap
    private var bitmapFlag: Bitmap

    init {
        paintBackground = Paint()
        paintBackground.color = Color.GRAY
        paintBackground.style = Paint.Style.FILL

        paintCover = Paint()
        paintCover.color = Color.LTGRAY
        paintCover.style = Paint.Style.FILL

        paintEmpty = Paint()
        paintEmpty.color = Color.TRANSPARENT
        paintBackground.style = Paint.Style.FILL

        paintLine = Paint()
        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f

        paintText = Paint()
        paintText.textSize = 100f
        paintText.color = Color.RED

        bitmapMine = BitmapFactory.decodeResource(resources, R.drawable.mine)
        bitmapFlag = BitmapFactory.decodeResource(resources, R.drawable.flag)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paintText.textSize = h/5f
        bitmapMine = Bitmap.createScaledBitmap(bitmapMine,
            width/5, height/5, false)
        bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag,
            width/5, height/5, false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)

        drawGameArea(canvas!!)
        drawModel(canvas)
        drawCover(canvas)
    }

    /*
    * SETUP:
    * Draws outline of the board: 4 vertical and 4 horizontal lines
    * */
    private fun drawGameArea(canvas: Canvas) {
        // border
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)

        drawLines(canvas)
    }
    private fun drawModel(canvas: Canvas) {
        for (i in 0..4) {
            for (j in 0..4) {
                val tX = (i * width / 5).toFloat()
                val tY = ((j+1) * height / 5).toFloat()

                when (MinesweeperModel.getFieldContent(i, j)) {
                    MinesweeperModel.MINE -> canvas.drawBitmap(bitmapMine, tX + (width / 40), tY - (height / 5), null)
                    MinesweeperModel.ONE -> canvas.drawText(MinesweeperModel.ONE.toString(), tX + (width / 30), tY- (height / 40), paintText)
                    MinesweeperModel.TWO -> canvas.drawText(MinesweeperModel.TWO.toString(), tX + (width / 30), tY- (height / 40), paintText)
                    MinesweeperModel.THREE -> canvas.drawText(MinesweeperModel.THREE.toString(), tX + (width / 30), tY- (height / 40), paintText)
                }
            }
        }
    }
    private fun drawCover(canvas: Canvas) {
        for (i in 0..4) {
            for (j in 0..4) {
                val tX = (i * width / 5).toFloat()
                val tY = (j * height / 5).toFloat()

                when (MinesweeperModel.getCoverContent(i, j)) {
                    MinesweeperModel.UNCLICKED -> canvas.drawRect(tX, tY, tX + (width / 5), tY + (height / 5),paintCover)
                    MinesweeperModel.CLICKED -> canvas.drawRect(tX, tY, tX + (width / 5), tY + (height / 5),paintEmpty)
                    MinesweeperModel.FLAG ->  {
                        canvas.drawRect(tX, tY, tX + (width / 5), tY + (height / 5),paintCover)
                        canvas.drawBitmap(bitmapFlag, tX + (width / 50), tY, null)
                    }
                }
            }
        }

        drawLines(canvas)
    }
    private fun drawLines(canvas: Canvas) {
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

    // Ends game
    private fun endGame() {
        // if nothing left, wins game
        if (checkUncovered()) {
            Log.i(null, "You won!")
            //MainActivity.snackBarMsg("You won!")
        } else {
            // MainActivity.snackBarMsg("You lost :(")
            Log.i(null, "You lost :(")
        }
        MinesweeperModel.setEndState(true)
        removeCover()
        invalidate()
    }

    private fun checkUncovered() : Boolean {
        for (i in 0..4) {
            for (j in 0..4) {
                if (MinesweeperModel.getCoverContent(i, j) == MinesweeperModel.UNCLICKED) return false
            }
        }
        return false
    }

    private fun removeCover() {
        for (i in 0..4) {
            for (j in 0..4) {
                MinesweeperModel.setCoverContent(i, j, MinesweeperModel.CLICKED)
            }
        }
    }

    fun resetGame() {
        MinesweeperModel.setEndState(false)

        MinesweeperModel.resetBoard()
        MinesweeperModel.setMines()
        MinesweeperModel.setCellCount()
        // draws the clean board
        invalidate()
    }

    fun setFlagMode(flagOn : Boolean) {
        if (flagOn) {
            MinesweeperModel.interaction = MinesweeperModel.FLAG
        } else {
            MinesweeperModel.interaction = MinesweeperModel.REVEAL
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if ((event?.action == MotionEvent.ACTION_DOWN) && (!MinesweeperModel.gameEnd)) {
            val tX = event.x.toInt() / (width / 5)
            val tY = event.y.toInt() / (height / 5)

            // hit a bomb -> end game
            if ((tX < 5 && tY < 5) &&
                (MinesweeperModel.getFieldContent(tX, tY) == MinesweeperModel.MINE) &&
                            (MinesweeperModel.interaction == MinesweeperModel.REVEAL)) {
                endGame()
            }

            if ((tX < 5 && tY < 5) &&
                (MinesweeperModel.getCoverContent(tX, tY) == MinesweeperModel.UNCLICKED)) {
                            MinesweeperModel.setCoverContent(tX, tY, MinesweeperModel.interaction)

//                if (MinesweeperModel.interaction == MinesweeperModel.REVEAL) {
//                    MinesweeperModel.reveal(tX, tY)
//                } else {
//                    MinesweeperModel.flagOrUnflag(tX, tY, MinesweeperModel.FLAG)
//                }

                invalidate()
            }
            // Unflagging
            else if ((tX < 5 && tY < 5) &&
                (MinesweeperModel.getCoverContent(tX, tY) == MinesweeperModel.FLAG) &&
                            (MinesweeperModel.interaction == MinesweeperModel.FLAG)) {
                MinesweeperModel.setCoverContent(tX, tY, MinesweeperModel.UNCLICKED)

                invalidate()
            }

            // checks for game end state
            if (checkUncovered()) {
                Log.i(null, "There's nothing left!")
                endGame()
            }
        }
        return true
    }



}