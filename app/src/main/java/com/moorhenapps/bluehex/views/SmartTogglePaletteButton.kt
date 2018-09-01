package com.moorhenapps.bluehex.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.moorhenapps.bluehex.R
import com.moorhenapps.bluehex.utils.Colour

class SmartTogglePaletteButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val otherButtons = ArrayList<SmartTogglePaletteButton>()
    private var paints: List<Paint>
    private val text: String
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18f, resources.displayMetrics)
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        isDither = true
    }
    private val outlineTextPaint = Paint(textPaint).apply {
        style = Paint.Style.STROKE
        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics)
    }

    val colour: Colour

    private var textSelectedColour = Color.BLACK
    private var textUnselectedColour = Color.WHITE
    private var textSelectedBkColour = Color.WHITE
    private var textUnselectedBkColour = Color.BLACK

    init {

        val array = context.obtainStyledAttributes(attrs, R.styleable.SmartTogglePaletteButton, defStyleAttr, 0)

        try {
            textSelectedColour = array.getColor(R.styleable.SmartTogglePaletteButton_textSelectedColour, Color.WHITE)
            textUnselectedColour = array.getColor(R.styleable.SmartTogglePaletteButton_textUnselectedColour, Color.BLACK)
            textSelectedBkColour = array.getColor(R.styleable.SmartTogglePaletteButton_textSelectedBkColour, Color.BLACK)
            textUnselectedBkColour = array.getColor(R.styleable.SmartTogglePaletteButton_textUnselectedBkColour, Color.WHITE)
        } catch (e: Exception) {
            //?
        } finally {
            array.recycle()
        }

        textPaint.color = textUnselectedColour
        outlineTextPaint.color = textUnselectedBkColour
        colour = Colour.valueOf(tag as String)

        text = resources.getString(colour.nameRes)
        paints = Colour.valueOf(tag as String).colours.map {
            Paint().apply {
                this.color = it
                this.style = Paint.Style.FILL
            }
        }

        viewTreeObserver.addOnGlobalLayoutListener {
            otherButtons.clear()
            val parent = parent as ViewGroup
            val count = parent.childCount
            for (i in 0 until count) {
                val child = parent.getChildAt(i)
                if (child is SmartTogglePaletteButton && child !== this@SmartTogglePaletteButton) {
                    otherButtons.add(child)
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        val colorWidth = (width - (paddingLeft + paddingRight)) / paints.size.toFloat()
        val viewTop = paddingTop.toFloat()
        val viewBottom = height - paddingBottom.toFloat()

        var start = paddingLeft.toFloat()
        paints.forEach {
            canvas.drawRect(start, viewTop, start + colorWidth, viewBottom, it)
            start += colorWidth
        }

        val x = width * 0.5f
        val y = height * 0.6f
        canvas.drawText(text, x, y, outlineTextPaint)
        canvas.drawText(text, x, y, textPaint)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selected) {
            for (button in otherButtons) {
                button.isSelected = false
            }
        }

        if (selected) {
            textPaint.color = textSelectedColour
            outlineTextPaint.color = textSelectedBkColour
        } else {
            textPaint.color = textUnselectedColour
            outlineTextPaint.color = textUnselectedBkColour
        }

        invalidate()
    }
}