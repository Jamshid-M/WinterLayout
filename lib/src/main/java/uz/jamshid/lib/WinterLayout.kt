package uz.jamshid.lib

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.graphics.drawable.toBitmap

class WinterLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var snows = arrayListOf<Snow>()
    private var animator: ValueAnimator? = null
    private var minAmplitude = 40
    private var maxAmplitude = 50
    private var minSpeed = 3
    private var maxSpeed = 7
    private var minSize = 20
    private var maxSize = 30
    private var bitmap: Bitmap? = null
    private var paused = true
    private var snowCount = 100
    private var stopInProcess = false

    init {
        setWillNotDraw(false)
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL

        val a = context.obtainStyledAttributes(attrs, R.styleable.WinterLayout)
        snowCount = a.getInt(R.styleable.WinterLayout_snowCount, 100)
        minAmplitude = a.getInt(R.styleable.WinterLayout_minAmplitude, 40)
        maxAmplitude = a.getInt(R.styleable.WinterLayout_maxAmplitude, 50)
        minSpeed = a.getInt(R.styleable.WinterLayout_minSpeed, 3)
        maxSpeed = a.getInt(R.styleable.WinterLayout_maxSpeed, 7)
        minSize = a.getInt(R.styleable.WinterLayout_minSize, 20)
        maxSize = a.getInt(R.styleable.WinterLayout_maxSize, 30)
        bitmap = a.getDrawable(R.styleable.WinterLayout_snowImage)?.toBitmap()

        a.recycle()
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)

        if(!paused) {
            snows.forEach {
                it.update()
                it.draw(canvas!!)
            }
        }
    }

    fun isPaused(): Boolean = paused

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        for (i in 0 until snowCount) {
            snows.add(Snow(Snow.Params(width, height, bitmap, minAmplitude, maxAmplitude,
                minSpeed, maxSpeed, minSize, maxSize)))
        }
    }

    fun startWinter(){
        snows.forEach {
            it.start()
        }

        stopInProcess = false

        if(animator?.isRunning == true)
            return


        animator = ValueAnimator.ofFloat(0f, 360f)
        animator?.addUpdateListener {
            invalidate()
        }
        animator?.repeatCount = -1
        animator?.start()
        paused = false
    }

    fun stopWinter(){
        if(stopInProcess)
            return
        stopInProcess = true
        var size = 0
        snows.forEach {
            it.stop {
                size++
                if(size == snowCount) {
                    animator?.cancel()
                    paused = true
                    stopInProcess = false
                    snows.forEach{
                        it.restart()
                    }
                }
            }
        }
    }

    fun stopImmediately(){
        animator?.cancel()
        paused = true
        invalidate()
        animator = null
        snows.forEach {
            it.restart()
        }
    }

    fun setSnowSize(size: Int, bitmap: Bitmap?=null, minAmplitude: Int=40, maxAmplitude: Int=50,
                    minSpeed: Int=3, maxSpeed: Int=7, minSize: Int=20, maxSize: Int = 30){
        snows.clear()
        snowCount = size

        this.bitmap = bitmap
        this.minAmplitude = minAmplitude
        this.maxAmplitude = maxAmplitude
        this.minSpeed = minSpeed
        this.maxSpeed = maxSpeed
        this.minSize = minSize
        this.maxSize = maxSize

    }

}