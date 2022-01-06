package kz.kakainchik.sportradarsample.ui.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kz.kakainchik.sportradarsample.R
import kz.kakainchik.sportradarsample.ext.dpToPx
import kotlin.math.roundToInt

class VolumeControl(
    context: Context,
    attrs: AttributeSet
) : View(context, attrs) {
    companion object {
        private const val DEFAULT_LINES: Int = 10
        private const val DEFAULT_MAX: Float = 100F
        private const val DEFAULT_VALUE: Float = 40F
        private const val DEFAULT_SIZE = 100
        private const val DEFAULT_ACTIVE_COLOR = Color.BLUE
        private const val DEFAULT_INACTIVE_COLOR = Color.LTGRAY
        private const val DEFAULT_LINE_HEIGHT = DEFAULT_SIZE / DEFAULT_LINES / 2
    }

    private val vlPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lnPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var vlRect = Rect()
    private var lnRect = Rect()
    private var lineHeight: Int = DEFAULT_LINE_HEIGHT

    var actColor: Int = DEFAULT_ACTIVE_COLOR
    var inactColor: Int = DEFAULT_INACTIVE_COLOR

    var linesCount: Int = DEFAULT_LINES
        get() = field
        set(value) {
            //There are collisions with huge numbers
            field = if(value > 150) DEFAULT_LINES
            else value
            postInvalidate()
        }
    var volValue: Float = DEFAULT_VALUE
        get() = field
        set(value) {
            if(value < 0 || value > DEFAULT_MAX) return
            field = value
            postInvalidate()
            onVolumeChangeListener?.invoke(field)
        }

    var onVolumeChangeListener: ((Float) -> Unit)? = null
        private set

    init {
        val ta: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.VolumeControl
        )
        linesCount = ta.getInteger(R.styleable.VolumeControl_lines, DEFAULT_LINES)
        volValue = ta.getFloat(R.styleable.VolumeControl_value, DEFAULT_VALUE)
        actColor = ta.getColor(R.styleable.VolumeControl_actColor, DEFAULT_ACTIVE_COLOR)
        inactColor = ta.getColor(R.styleable.VolumeControl_inactColor, DEFAULT_INACTIVE_COLOR)

        ta.recycle()

        setup()
    }

    fun setOnVolumeChangeListener(listener: (Float) -> Unit) {
        this.onVolumeChangeListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val initWidth = resolveDefaultSize(widthMeasureSpec)
        val initHeight = resolveDefaultSize(heightMeasureSpec)
        //Always! should be invoked
        setMeasuredDimension(initWidth, initHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if(w == 0 || h == 0) return
    }

    override fun onDraw(canvas: Canvas) {
        val vlRectCount = (linesCount / 100F * volValue).roundToInt()
        val lnRectCount = linesCount - vlRectCount

        lineHeight = height / linesCount / 2

        //Rect for inactive lines
        lnRect.apply {
            left = 0
            top = 0
            right = width
            bottom = top + lineHeight
        }
        //Rect for active lines
        vlRect.apply {
            left = 0
            top = 0
            right = width
            bottom = top + lineHeight
        }

        //Draw inactive lines
        for(a in 0 until lnRectCount) {
            lnRect.offsetTo(0, a * lineHeight * 2)
            canvas.drawRect(lnRect, lnPaint)
        }
        //Draw active lines
        for(a in 0 until vlRectCount) {
            vlRect.offsetTo(0, (a + lnRectCount) * lineHeight * 2)
            canvas.drawRect(vlRect, vlPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        volValue = ((height - event.y) * DEFAULT_MAX) / height
        if(volValue < 0) return false

        //Invoke invalidation in setter field
        return true
    }

    private fun resolveDefaultSize(spec: Int): Int {
        val specSize = MeasureSpec.getSize(spec)

        return when(MeasureSpec.getMode(spec)) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> specSize
            MeasureSpec.UNSPECIFIED -> {
                //Default size
                context.dpToPx(DEFAULT_SIZE).toInt()
            }
            else -> MeasureSpec.getSize(spec)
        }
    }

    private fun setup() {
        with(vlPaint) {
            color = actColor
            style = Paint.Style.FILL
        }

        with(lnPaint) {
            color = inactColor
            style = Paint.Style.FILL
        }
    }
}