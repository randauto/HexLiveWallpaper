package com.moorhenapps.bluehex.wallpaper;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class HexView extends View {
    public ColouredHexDrawer hexDrawer;

    public HexView(Context context) {
        this(context, null, 0, 0);
    }

    public HexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public HexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        hexDrawer = new ColouredHexDrawer();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        hexDrawer.draw(canvas);
    }
}
