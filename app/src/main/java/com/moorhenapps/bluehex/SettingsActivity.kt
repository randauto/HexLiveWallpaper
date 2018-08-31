package com.moorhenapps.bluehex

import android.app.Activity
import android.os.Bundle
import android.view.View

import com.moorhenapps.bluehex.utils.Colour
import com.moorhenapps.bluehex.utils.PrefsHelper
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : Activity() {

    private val sizeButtons by lazy { listOf(size_small, size_medium, size_large) }
    private val speedButtons by lazy { listOf(speed_slow, speed_medium, speed_fast) }
    private val colourButtons by lazy { listOf(colour_blue, colour_green, colour_red, colour_grey, colour_rainbow, colour_dark, colour_pastel, colour_gold, colour_nexus, colour_purple, colour_retro, colour_pink) }

    private lateinit var prefsHelper: PrefsHelper

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val onSizeClick = View.OnClickListener { view ->
            val size = PrefsHelper.Size.valueOf(view.tag as String)
            prefsHelper.size = size
            view.isSelected = true
        }

        val onColourClick = View.OnClickListener { view ->
            val colour = Colour.valueOf(view.tag as String)
            prefsHelper.colour = colour
            view.isSelected = true
            (sizeButtons + speedButtons).forEach { it.accentColour = colour.colours[0] }
        }

        val onSpeedClick = View.OnClickListener { view ->
            val speed = PrefsHelper.Speed.valueOf(view.tag as String)
            prefsHelper.speed = speed
            view.isSelected = true
        }

        sizeButtons.forEach { it.setOnClickListener(onSizeClick) }
        speedButtons.forEach { it.setOnClickListener(onSpeedClick) }
        colourButtons.forEach { it.setOnClickListener(onColourClick) }

        prefsHelper = PrefsHelper(this)
    }

    override fun onResume() {
        super.onResume()

        val colour = prefsHelper.colour
        val speed = prefsHelper.speed
        val size = prefsHelper.size

        colourButtons.first { it.tag == colour.name }.isSelected = true
        sizeButtons.first { it.tag == size.name }.isSelected = true
        speedButtons.first { it.tag == speed.name }.isSelected = true

    }
}
