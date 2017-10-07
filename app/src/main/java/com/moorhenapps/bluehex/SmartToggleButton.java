package com.moorhenapps.bluehex;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import java.util.ArrayList;

public class SmartToggleButton extends Button {
    private int accentColour;
    private ArrayList<SmartToggleButton> otherButtons = new ArrayList<>();

    public SmartToggleButton(Context context) {
        this(context, null, 0);
    }

    public SmartToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        accentColour = Color.parseColor("#2778F3");
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                otherButtons.clear();
                ViewGroup parent = (ViewGroup) getParent();
                int count = parent.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = parent.getChildAt(i);
                    if (child instanceof SmartToggleButton && child != SmartToggleButton.this) {
                        otherButtons.add((SmartToggleButton) child);
                    }
                }
            }
        });
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            for (SmartToggleButton button : otherButtons) {
                button.setSelected(false);
            }
        }

        setTextColor(selected ? accentColour : Color.BLACK);
    }
}