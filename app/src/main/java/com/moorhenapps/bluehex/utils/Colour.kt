package com.moorhenapps.bluehex.utils

import android.graphics.Color

enum class Colour constructor(vararg colourList: Int) {
    BLUE(
            Color.parseColor("#239dce"),
            Color.parseColor("#2192bc"),
            Color.parseColor("#2196c2"),
            Color.parseColor("#2298c6"),
            Color.parseColor("#1f8eb9"),
            Color.parseColor("#24a0d3"),
            Color.parseColor("#229dc9"),
            Color.parseColor("#2191bf"),
            Color.parseColor("#2298c6"),
            Color.parseColor("#1b7ba3"),
            Color.parseColor("#59c2ee"),
            Color.parseColor("#0c88f4"),
            Color.parseColor("#1a76e1")
    ),

    GREEN(
            Color.parseColor("#81C784"),
            Color.parseColor("#43A047"),
            Color.parseColor("#2E7D32"),
            Color.parseColor("#1B5E20"),
            Color.parseColor("#6D4C41"),
            Color.parseColor("#7CB342"),
            Color.parseColor("#558B2F")
    ),

    PURPLE(
            Color.parseColor("#BA68C8"),
            Color.parseColor("#9C27B0"),
            Color.parseColor("#7B1FA2"),
            Color.parseColor("#6A1B9A"),
            Color.parseColor("#4A148C"),
            Color.parseColor("#AA00FF")
    ),

    SPECTRUM(
            Color.parseColor("#ff0000"),
            Color.parseColor("#00ff00"),
            Color.parseColor("#0000ff"),
            Color.parseColor("#00ffff"),
            Color.parseColor("#ffff00"),
            Color.parseColor("#4b0082"),
            Color.parseColor("#551a8b"),
            Color.parseColor("#ffa500")
    );

    var colourList: IntArray

    init {
        this.colourList = colourList
    }
}
