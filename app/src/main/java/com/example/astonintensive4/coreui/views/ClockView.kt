package com.example.astonintensive4.coreui.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.astonintensive4.R
import com.example.astonintensive4.coreui.IPopulatable
import com.example.astonintensive4.utils.*
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr),
    IPopulatable<ClockViewModel> {

    private lateinit var mainCanvas: Canvas
    private lateinit var bitmapClockFace: Bitmap

    private var radius = 0f
    private var xCenter = 0f
    private var yCenter = 0f
    private val paint = Paint()

    private val numbersRadius
        get() = if (radius > 0f)
            radius - resources.getDimensionPixelOffset(R.dimen.clock_view_numbers_margin)
        else
            0f

    private val defaultSecondHandLength: Int
        get() = if (numbersRadius > 0)
            (numbersRadius - resources.getDimensionPixelOffset(R.dimen.clock_view_default_second_margin)).toInt()
        else 0

    private val defaultMinuteHandLength: Int
        get() = if (numbersRadius > 0)
            (numbersRadius - resources.getDimensionPixelOffset(R.dimen.clock_view_default_minute_margin)).toInt()
        else 0

    private val defaultHourHandLength: Int
        get() = if (numbersRadius > 0)
            (numbersRadius - resources.getDimensionPixelOffset(R.dimen.clock_view_default_hour_margin)).toInt()
        else 0

    private val defaultColor = resources.getColor(R.color.default_hand_color, context.theme)

    private var hourHandColor = defaultColor
    private var minuteHandColor = defaultColor
    private var secondHandColor = defaultColor

    private var secondHandLength = 0
    private var minuteHandLength = 0
    private var hourHandLength = 0

    private var secondHandWidth = 0f
    private var minuteHandWidth = 0f
    private var hourHandWidth = 0f

    fun updateClock() {
        this.invalidate()
    }

    init {
        applyAttrs(context.theme.obtainStyledAttributes(attrs, R.styleable.ClockView, 0, 0))
    }

    override fun populate(model: ClockViewModel) {
        hourHandColor = model.hourHandColor
        minuteHandColor = model.minuteHandColor
        secondHandColor = model.secondHandColor
        secondHandLength = model.secondHandLength
        minuteHandLength = model.minuteHandLength
        hourHandLength = model.hourHandLength
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (::bitmapClockFace.isInitialized) bitmapClockFace.recycle()

        bitmapClockFace = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mainCanvas = Canvas(bitmapClockFace)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawClock(mainCanvas)
        canvas?.drawBitmap(bitmapClockFace, 0f, 0f, null)
    }

    private fun drawClock(canvas: Canvas) {
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR)

        val clockMargin = resources.getDimensionPixelOffset(R.dimen.clock_view_margin).toFloat()
        radius = if (height > width) width / 2f - clockMargin else height / 2f - clockMargin

        xCenter = width / 2f
        yCenter = height / 2f

        paint.apply {
            color = resources.getColor(R.color.clock_face, context.theme)
            strokeWidth = 2f
            style = Paint.Style.STROKE
            isAntiAlias = true
            textSize = resources.getDimensionPixelOffset(R.dimen.clock_fontSize).toFloat()
            textAlign = Paint.Align.CENTER
        }

        drawClockFace(canvas)
        drawHands(canvas)
    }

    private fun drawClockFace(canvas: Canvas) {
        canvas.drawCircle(xCenter, yCenter, radius, paint)
        paint.style = Paint.Style.FILL
        val centerDotRadius =
            resources.getDimensionPixelOffset(R.dimen.clock_view_center_dot_radius).toFloat()
        canvas.drawCircle(xCenter, yCenter, centerDotRadius, paint)
        drawClockNumber(canvas)
    }

    private fun drawClockNumber(canvas: Canvas) {
        for (num in 1..12) {
            val xNumber = getXForClockNumber(num, xCenter, numbersRadius)
            val yNumber = getYForClockNumber(num, yCenter, numbersRadius)
            if (num <= 9) {
                canvas.drawText(num.toString(),
                    xNumber - 5f,
                    yNumber + 10f,
                    paint)
            } else {
                canvas.drawText(num.toString(),
                    xNumber - 15f,
                    yNumber + 10f,
                    paint)
            }
        }
    }

    private fun drawHand(
        canvas: Canvas,
        handLength: Int,
        handAngle: Float,
        handColor: Int,
        handWidth: Float,
    ) {
        paint.color = handColor
        paint.strokeWidth = handWidth
        canvas.drawLine(xCenter,
            yCenter,
            xCenter + handLength * cos(PI / 2f - handAngle * (PI / 180f)).toFloat(),
            yCenter - handLength * sin(PI / 2f - handAngle * (PI / 180F)).toFloat(),
            paint)
    }

    private fun drawHands(canvas: Canvas) {
        secondHandLength = if (secondHandLength > numbersRadius || secondHandLength == 0)
            defaultSecondHandLength
        else secondHandLength

        minuteHandLength = if (minuteHandLength > numbersRadius || minuteHandLength == 0)
            defaultMinuteHandLength
        else minuteHandLength

        hourHandLength = if (hourHandLength > numbersRadius || hourHandLength == 0)
            defaultHourHandLength
        else hourHandLength

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        val seconds = calendar.get(Calendar.SECOND)

        val secondAngle: Float = getSecondAngle(seconds)
        val minuteAngle: Float = getMinuteAngle(seconds, minutes)
        val hourAngle: Float = getHourAngle(hour, minutes)

        drawHand(canvas, secondHandLength, secondAngle, secondHandColor, secondHandWidth)
        drawHand(canvas, minuteHandLength, minuteAngle, minuteHandColor, minuteHandWidth)
        drawHand(canvas, hourHandLength, hourAngle, hourHandColor, hourHandWidth)
    }

    private fun applyAttrs(attrs: TypedArray) {
        with(attrs) {
            secondHandColor =
                getColor(R.styleable.ClockView_SecondHandColor, context.getColor(R.color.default_hand_color))
            minuteHandColor =
                getColor(R.styleable.ClockView_MinuteHandColor, context.getColor(R.color.default_hand_color))
            hourHandColor =
                getColor(R.styleable.ClockView_HourHandColor, context.getColor(R.color.default_hand_color))

            secondHandLength = getDimensionPixelOffset(R.styleable.ClockView_SecondHandLength, 0)
            minuteHandLength = getDimensionPixelOffset(R.styleable.ClockView_MinuteHandLength, 0)
            hourHandLength = getDimensionPixelOffset(R.styleable.ClockView_HourHandLength, 0)

            secondHandWidth =
                getDimensionPixelOffset(R.styleable.ClockView_SecondHandWidth,
                    resources.getDimensionPixelOffset(R.dimen.clock_hand_default_width)).toFloat()
            minuteHandWidth =
                getDimensionPixelOffset(R.styleable.ClockView_MinuteHandWidth,
                    resources.getDimensionPixelOffset(R.dimen.clock_hand_default_width)).toFloat()
            hourHandWidth =
                getDimensionPixelOffset(R.styleable.ClockView_HourHandWidth,
                    resources.getDimensionPixelOffset(R.dimen.clock_hand_default_width)).toFloat()
        }
    }
}