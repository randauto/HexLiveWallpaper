package com.moorhenapps.bluehex.utils;

import android.graphics.Color;

public enum Colour {
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

    GREY(
            Color.parseColor("#CCCCCC"),
            Color.parseColor("#FFFFFF"),
            Color.parseColor("#000000"),
            Color.parseColor("#838383"),
            Color.parseColor("#AAAAAA"),
            Color.parseColor("#333333"),
            Color.parseColor("#555555")
    ),

    PURPLE(
            Color.parseColor("#BA68C8"),
            Color.parseColor("#9C27B0"),
            Color.parseColor("#7B1FA2"),
            Color.parseColor("#76008A"),
            Color.parseColor("#6A1B9A"),
            Color.parseColor("#4A148C"),
            Color.parseColor("#6D2D78"),
            Color.parseColor("#AA00FF")
    ),

    RED(
            Color.parseColor("#FF5733"),
            Color.parseColor("#F13C15"),
            Color.parseColor("#BA3316"),
            Color.parseColor("#911B01"),
            Color.parseColor("#EC7358"),
            Color.parseColor("#ED1B0E"),
            Color.parseColor("#FF9F0C"),
            Color.parseColor("#870923")
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

    public final int[] colours;

    Colour(int... colours) {
      this.colours = colours;
    }
}
