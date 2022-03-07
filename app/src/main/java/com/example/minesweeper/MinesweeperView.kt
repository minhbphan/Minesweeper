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
        paintBackground = Paint()
        paintBackground.color = Color.GRAY
        paintBackground.style = Paint.Style.FILL

        paintCover = Paint()
        paintCover.color = Color.MAGENTA
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

    // TODO: draw the cover
    private fun drawCover(canvas: Canvas) {
        for (i in 0..4) {
            for (j in 0..4) {
                val tX = (i * width / 5).toFloat()
                val tY = (j * height / 5).toFloat()

                when (MinesweeperModel.getCoverContent(i, j)) {
                    MinesweeperModel.UNCLICKED -> canvas.drawRect(tX, tY, tX + (width / 5), tY + (height / 5),paintCover)
                    MinesweeperModel.REVEAL -> canvas.drawRect(tX, tY, tX + (width / 5), tY + (height / 5),paintEmpty)
                    MinesweeperModel.FLAG -> canvas.drawBitmap(bitmapFlag, tX + (width / 40), tY - (height / 5), null)
                }
            }
        }

        drawLines(canvas)
    }

    fun resetGame() {
        MinesweeperModel.resetBoard()
        MinesweeperModel.setMines()
        MinesweeperModel.setCellCount()
        // draws the clean board
        invalidate()
    }

    // TODO: add functionality for reveal and flagging interactions
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val tX = event.x.toInt() / (width / 5)
            val tY = event.y.toInt() / (height / 5)

            if ((tX < 5 && tY < 5) && (MinesweeperModel.getCoverContent(tX, tY) == MinesweeperModel.UNCLICKED)) {
                MinesweeperModel.reveal(tX, tY)
                invalidate()
            }

        }
        return true
    }



}