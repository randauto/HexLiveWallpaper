package com.moorhenapps.bluehex.wallpaper;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class HexView extends View implements Runnable{
    public ColouredHexDrawer hexDrawer;

    public HexView(Context context) {
        this(context, null, 0);
    }

    public HexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HexView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this);
    }

    private void init() {
        hexDrawer = new ColouredHexDrawer();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        hexDrawer.draw(canvas);
    }

    @Override
    public void run() {
        postInvalidate();
        postDelayed(this, 15);
    }
}
