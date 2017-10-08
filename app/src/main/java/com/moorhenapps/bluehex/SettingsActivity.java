package com.moorhenapps.bluehex;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.moorhenapps.bluehex.utils.Colour;
import com.moorhenapps.bluehex.utils.PrefsHelper;

public class SettingsActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    private PrefsHelper prefsHelper;

    private View root;

    private SeekBar speedBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        root = findViewById(R.id.root);

        speedBar = findViewById(R.id.speed);

        findViewById(R.id.blue).setOnClickListener(onColourClick);
        findViewById(R.id.green).setOnClickListener(onColourClick);
        findViewById(R.id.red).setOnClickListener(onColourClick);
        findViewById(R.id.grey).setOnClickListener(onColourClick);
        findViewById(R.id.purple).setOnClickListener(onColourClick);

        findViewById(R.id.tiny).setOnClickListener(onSizeClick);
        findViewById(R.id.small).setOnClickListener(onSizeClick);
        findViewById(R.id.size_normal).setOnClickListener(onSizeClick);
        findViewById(R.id.big).setOnClickListener(onSizeClick);
        findViewById(R.id.large).setOnClickListener(onSizeClick);

        prefsHelper = new PrefsHelper(this);

        speedBar.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Colour colour = prefsHelper.getColour();
        int speedSetting = prefsHelper.getSpeedSetting();
        PrefsHelper.Size size = prefsHelper.getSize();

        View colourButton = root.findViewWithTag(colour.name());
        if (colourButton != null) {
            colourButton.performClick();
        } else {
            findViewById(R.id.blue).performClick();
        }

        View sizeButton = root.findViewWithTag(size.name());
        if (sizeButton != null) {
            sizeButton.performClick();
        } else {
            findViewById(R.id.size_normal).performClick();
        }

        speedBar.setProgress(speedSetting - 1);

        Log.d("Settings", String.format("Speed: %s, Size: %s, Colour: %s", speedSetting, size, colour));
    }

    private View.OnClickListener onSizeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PrefsHelper.Size size = PrefsHelper.Size.valueOf((String) view.getTag());
            prefsHelper.setSize(size);
            view.setSelected(true);
        }
    };

    private View.OnClickListener onColourClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Colour colour = Colour.valueOf((String) view.getTag());
            prefsHelper.setColour(colour);
            view.setSelected(true);
        }
    };

    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
        prefsHelper.setSpeedSetting(progress + 1);
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {

    }
}
