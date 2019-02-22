package com.moorhenapps.bluehex.wallpaper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.moorhenapps.bluehex.utils.PrefsHelper;

public class HexView extends View implements Runnable, SharedPreferences.OnSharedPreferenceChangeListener {
    private ColouredHexDrawer hexDrawer;
    private PrefsHelper prefsHelper;

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

    private void init() {
        hexDrawer = new ColouredHexDrawer();
        prefsHelper = new PrefsHelper(getContext());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(new Runnable() {
            @Override
            public void run() {
                hexDrawer.setCanvasSize(getWidth(), getHeight());
                updateHexDrawer();
            }
        });
        post(this);
        prefsHelper.getPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this);
        prefsHelper.getPreferences().unregisterOnSharedPreferenceChangeListener(this);
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

    private void updateHexDrawer() {
        hexDrawer.setColour(prefsHelper.getColour());
        if (prefsHelper.getAdvancedSettings()) {
            hexDrawer.setSpeed(prefsHelper.getAdvSpeed());
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, prefsHelper.getAdvSize(), getResources().getDisplayMetrics());
            hexDrawer.setTileSize(size);
        } else {
            hexDrawer.setSpeed(prefsHelper.getSpeed().getMs());
            hexDrawer.setTileSize(prefsHelper.getSize().dp(getResources()));
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateHexDrawer();
    }
}
