package uz.jamshid.lib

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

class Snow(val params: Params) {

    private var positionX = 0f                                      //position of snowflake on X coordinate
    private var positionY = 0f                                      //position of snowflake on Y coordinate
    private var range = Random.nextInt(params.minAmplitude, params.maxAmplitude)          //range for amplitude
    private var speedY = Random.nextInt(params.minSpeed,params.maxSpeed)            //speed on Y coordinate
    private var speedX = Random.nextInt(45, 65)         //speed on X coordinate
    private var startingOffset = Random.nextFloat()                 //for initializing starting point of snowflake
    private var amplitude = params.parentWidth * range/100         // amplitude for sin function, depends on range field
    private var horizontalOffset = Random.nextInt(4,7)  //horizontal offset, for keeping snowflakes on screen
    private var degree = 0                                          //degree for sin function
    private var size = Random.nextInt(params.minSize, params.maxSize)           //size of snow
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bitmap: Bitmap? = null

    private var stopped = false
    private var destroyed = false
    private var callback: (()-> Unit)?= null

    init {
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        positionY = (-Random.nextInt(1, params.parentHeight)).toFloat()
        positionX = params.parentWidth/2f
        if(params.bitmap!=null) {
            bitmap = Bitmap.createScaledBitmap(params.bitmap, size, size, false)
        }
    }

    fun draw(canvas: Canvas){
        if(!destroyed && isOnScreen()) {
            if (bitmap != null)
                canvas.drawBitmap(bitmap!!, positionX, positionY, paint)
            else
                canvas.drawCircle(positionX, positionY, size.toFloat(), paint)
        }
    }

    private fun isOnScreen(): Boolean{
        return positionY > 0 && positionY < params.parentHeight
    }

    fun update(){
        if(destroyed)
            return

        val radians = (PI / amplitude) * degree
        val sin = sin(radians)
        degree++

        positionX = (sin * params.parentWidth/ horizontalOffset * speedX/100 + params.parentWidth*startingOffset).toFloat()
        positionY += speedY

        if(positionY-size > params.parentHeight)
            reset()
    }

    fun stop(callback: (() -> Unit)){
        stopped = true
        this.callback = callback
    }

    private fun reset(){
        if(stopped) {
            destroyed = true
            callback?.invoke()
        }

        positionY = (-size).toFloat()
        speedY = Random.nextInt(params.minSpeed, params.maxSpeed)
        speedX = Random.nextInt(45, 65)
        startingOffset = Random.nextFloat()
        amplitude = params.parentWidth * range / 100
        horizontalOffset = Random.nextInt(4, 7)
        size = Random.nextInt(params.minSize, params.maxSize)
        if (params.bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(params.bitmap, size, size, false)
        }

    }

    fun restart(){
        reset()
        positionY = (-Random.nextInt(1, params.parentHeight)).toFloat()
        positionX = params.parentWidth/2f
    }

    fun start() {
        stopped = false
        callback = null
        destroyed = false
    }

    data class Params(val parentWidth: Int, val parentHeight: Int, val bitmap: Bitmap? = null,
                      val minAmplitude: Int, val maxAmplitude: Int, val minSpeed: Int, val maxSpeed: Int,
                      val minSize: Int, val maxSize: Int)
}