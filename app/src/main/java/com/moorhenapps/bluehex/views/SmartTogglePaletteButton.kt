package com.moorhenapps.bluehex.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Button
import com.moorhenapps.bluehex.R
import com.moorhenapps.bluehex.utils.Colour

class SmartTogglePaletteButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : Button(context, attrs, defStyleAttr) {

    private val otherButtons = ArrayList<SmartTogglePaletteButton>()
    private var paints: List<Paint>
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

        colour = Colour.valueOf(tag as String)

        setText(colour.nameRes)
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

        setShadow(textUnselectedBkColour)
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

        super.onDraw(canvas)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selected) {
            for (button in otherButtons) {
                button.isSelected = false
            }
        }
        setTextColor(if (selected) textSelectedColour else textUnselectedColour)

        if (selected) {
            setShadow(textSelectedBkColour)
        } else {
            setShadow(textUnselectedBkColour)
        }

        invalidate()
    }

    private fun setShadow(colour: Int) {
        setShadowLayer(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics), 0f, 0f, colour)
    }
}