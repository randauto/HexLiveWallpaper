package com.moorhenapps.bluehex;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.moorhenapps.bluehex.utils.Colour;
import com.moorhenapps.bluehex.utils.PrefsHelper;
import com.moorhenapps.bluehex.wallpaper.GridWallpaper;
import com.moorhenapps.bluehex.wallpaper.HexView;

import static com.moorhenapps.bluehex.utils.PrefsHelper.MAX_SPEED_OFFSET;

public class SettingsActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    private PrefsHelper prefsHelper;

    private View root;
	private Colour colour;
	private int speed;
	private PrefsHelper.Size size;

	private HexView exampleView;

	private SeekBar speedBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        root = findViewById(R.id.root);

        exampleView = findViewById(R.id.hex_example);
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

        exampleView.post(new Runnable() {
			@Override
			public void run() {
				exampleView.hexDrawer.setCanvasSize(exampleView.getWidth(), exampleView.getHeight());
			}
		});

		speedBar.setOnSeekBarChangeListener(this);

        findViewById(R.id.install).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(SettingsActivity.this, GridWallpaper.class));
                startActivity(intent);
                finish();
            }
        });
    }

	private void runDemo(){
		exampleView.postDelayed(invalidateExample, 8);
		exampleView.invalidate();
	}

	private Runnable invalidateExample = new Runnable() {
		@Override
		public void run() {
			runDemo();
		}
	};

    @Override
    protected void onResume() {
        super.onResume();

		colour = prefsHelper.getColour();
        speed = prefsHelper.getSpeed();
        size = prefsHelper.getSize();

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

		int idx = (speed - MAX_SPEED_OFFSET) / 500;
		speedBar.setProgress(idx);

        exampleView.hexDrawer.setSpeed(speed);


        Log.d("Settings", String.format("Speed: %s, Size: %s, Colour: %s", speed, size, colour));

		runDemo();
    }

    @Override
    protected void onPause() {
        super.onPause();
		Log.d("Settings", String.format("Speed: %s, Size: %s, Colour: %s", speed, size, colour));
        prefsHelper.setSpeed(speed);
        prefsHelper.setSize(size);
        prefsHelper.setColour(colour);
		exampleView.removeCallbacks(invalidateExample);
    }

    private View.OnClickListener onSizeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            size = PrefsHelper.Size.valueOf((String) view.getTag());
            exampleView.hexDrawer.setTileSize(size.getDp(getResources()));
            view.setSelected(true);
        }
    };

	private View.OnClickListener onColourClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			colour = Colour.valueOf((String) view.getTag());
			exampleView.hexDrawer.setColour(colour);
            view.setSelected(true);
		}
	};

	@Override
	public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
		exampleView.hexDrawer.setSpeed((500 * seekBar.getMax() + MAX_SPEED_OFFSET) - (progress * 500));
	}

	@Override
	public void onStartTrackingTouch(final SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(final SeekBar seekBar) {

	}
}
