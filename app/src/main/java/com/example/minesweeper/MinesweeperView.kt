package com.example.minesweeper

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

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
        // background of the field
        paintBackground = Paint()
        paintBackground.color = Color.GRAY
        paintBackground.style = Paint.Style.FILL

        // cover
        paintCover = Paint()
        paintCover.color = Color.LTGRAY
        paintCover.style = Paint.Style.FILL

        // removes the cover
        paintEmpty = Paint()
        paintEmpty.color = Color.TRANSPARENT
        paintBackground.style = Paint.Style.FILL

        // lines
        paintLine = Paint()
        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f

        // labels the adjacent mines count
        paintText = Paint()
        paintText.textSize = 100f
        paintText.color = Color.RED

        // images of flags and mines
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // only allows interactions with the board if game hasn't ended
        if ((event?.action == MotionEvent.ACTION_DOWN) && !(MinesweeperModel.gameEnd)) {

            val tX = event.x.toInt() / (width / 5)
            val tY = event.y.toInt() / (height / 5)

            // SCENARIO 1: player hits a bomb, end game (Lose)
            if ((tX < 5 && tY < 5) &&
                (MinesweeperModel.getFieldContent(tX, tY) == MinesweeperModel.MINE) &&
                (MinesweeperModel.interaction == MinesweeperModel.REVEAL)) {
                endGame()
                return true
            }

            // SCENARIO 2: player either reveals a hidden cell OR
            // player places a flag on a suspected mine
            if ((tX < 5 && tY < 5) &&
                (MinesweeperModel.getCoverContent(tX, tY) == MinesweeperModel.HIDDEN)) {
                    // Reveal mode
                if (MinesweeperModel.interaction == MinesweeperModel.REVEAL) {
                    MinesweeperModel.setCoverContent(tX, tY, MinesweeperModel.REVEAL)
                }
                    // Flag mode
                else {
                    val flagCount = MinesweeperModel.getflagRemaining()
                    // only flag if there are flags remaining
                    if (flagCount > 0) {
                        MinesweeperModel.setCoverContent(tX, tY, MinesweeperModel.FLAG)
                        MinesweeperModel.setflagRemaining(flagCount - 1)
                    }
                }
                invalidate()
            }

            // SCENARIO 3: player unflags a cell that is currently flagged
            else if ((tX < 5 && tY < 5) &&
                (MinesweeperModel.getCoverContent(tX, tY) == MinesweeperModel.FLAG) &&
                (MinesweeperModel.interaction == MinesweeperModel.FLAG)) {
                val flagCount = MinesweeperModel.getflagRemaining()
                MinesweeperModel.setflagRemaining(flagCount + 1)
                MinesweeperModel.setCoverContent(tX, tY, MinesweeperModel.HIDDEN)

                invalidate()
            }

            // after each move, check for
            // SCENARIO 4: player reveals/flags all cells, winning the game
            if (MinesweeperModel.winningUncovered()) {
                endGame()
            }

            // updates with remaining flags
            (context as MainActivity).updateRemainingFlags(MinesweeperModel.getflagRemaining())
        }

        return true
    }

    /* ***** SETUP: draws outline of the board (4 vertical and 4 horizontal lines) ***** */
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
                    MinesweeperModel.HIDDEN -> canvas.drawRect(tX, tY, tX + (width / 5), tY + (height / 5),paintCover)
                    MinesweeperModel.SHOWN -> canvas.drawRect(tX, tY, tX + (width / 5), tY + (height / 5),paintEmpty)
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

    /* ***** Helper Methods: functionality for ending and resetting game, *****
       ***** setting appropriate interaction on cells (revealing or flagging) ***** */
    private fun endGame() {
        // if nothing left, wins game
        if (MinesweeperModel.winningUncovered()) {
            (context as MainActivity).snackBarMsg(context.getString(R.string.win_msg))
        } else {
            (context as MainActivity).snackBarMsg(context.getString(R.string.lose_msg))
        }
        MinesweeperModel.setEndState(true)
        MinesweeperModel.removeCover()
        invalidate()
    }
    fun resetGame() {
        MinesweeperModel.setEndState(false)
        MinesweeperModel.resetBoard()
        MinesweeperModel.setMines()
        MinesweeperModel.setCellCount()
        (context as MainActivity).updateRemainingFlags(MinesweeperModel.getflagRemaining())
        (context as MainActivity).snackBarMsg(context.getString(R.string.new_game))
        // draws the clean board
        invalidate()
    }
    fun setFlagMode(flagOn : Boolean) {
        if (flagOn) {
            MinesweeperModel.interaction = MinesweeperModel.FLAG
            (context as MainActivity).snackBarMsg(context.getString(R.string.flag_on))
        } else {
            MinesweeperModel.interaction = MinesweeperModel.REVEAL
            (context as MainActivity).snackBarMsg(context.getString(R.string.flag_off))
        }
    }

}