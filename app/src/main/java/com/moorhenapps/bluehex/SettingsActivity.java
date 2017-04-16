package com.moorhenapps.bluehex;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.moorhenapps.bluehex.utils.Colour;
import com.moorhenapps.bluehex.utils.PrefsHelper;
import com.moorhenapps.bluehex.wallpaper.GridWallpaper;
import com.moorhenapps.bluehex.wallpaper.HexView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moorhenapps.bluehex.utils.PrefsHelper.MAX_SPEED_OFFSET;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private PrefsHelper prefsHelper;

	private Colour colour;
	private int speed;
	private PrefsHelper.Size size;

	@Bind(R.id.hex_example)
	HexView exampleView;

	@Bind(R.id.speed) SeekBar speedBar;

	@Bind(R.id.tiny) TextView sizeTiny;
	@Bind(R.id.small) TextView sizeSmall;
	@Bind(R.id.size_normal) TextView sizeNormal;
	@Bind(R.id.big) TextView sizeBig;
	@Bind(R.id.large) TextView sizeLarge;
	@Bind({R.id.tiny, R.id.small, R.id.size_normal, R.id.big, R.id.large}) View[] sizeButtons;

	@Bind(R.id.blue) TextView colourBlue;
	@Bind(R.id.green) TextView colourGreen;
	@Bind(R.id.purple) TextView colourPurple;
	@Bind(R.id.spectrum) TextView colourSpectrum;
	@Bind({R.id.blue, R.id.green, R.id.purple, R.id.spectrum}) View[] colourButtons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
		ButterKnife.bind(this);

        prefsHelper = new PrefsHelper(this);

        exampleView.post(new Runnable() {
			@Override
			public void run() {
				exampleView.getHexDrawer().setCanvasSize(exampleView.getWidth(), exampleView.getHeight());
			}
		});

		speedBar.setOnSeekBarChangeListener(this);
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
		if(!validateToggleBar(colour.name(), colourButtons)){
			colour = Colour.BLUE;
			colourBlue.setBackgroundResource(R.drawable.selection_bar);
		}

		speed = prefsHelper.getSpeed();
		int idx = (speed - MAX_SPEED_OFFSET) / 500;
		speedBar.setProgress(idx);

		size = prefsHelper.getSize();
		if(!validateToggleBar(size.name(), sizeButtons)){
			size = PrefsHelper.Size.NORMAL;
			sizeNormal.setBackgroundResource(R.drawable.selection_bar);
		}

        exampleView.getHexDrawer().setTileSize(size.getDp(getResources()));
        exampleView.getHexDrawer().setColour(colour);
        exampleView.getHexDrawer().setSpeed(speed);
        Log.d("Settings", String.format("Speed: %s, Size: %s, Colour: %s", speed, size, colour));

		runDemo();
    }

	private boolean validateToggleBar(String target, View[] buttons){
		for(View view : buttons){
			if(view.getTag().equals(target)){
				view.setBackgroundResource(R.drawable.selection_bar);
				return true;
			}
		}
		return false;
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

	@OnClick(R.id.install)
	public void onInstallClick(View view){
		Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
		intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(SettingsActivity.this, GridWallpaper.class));
		startActivity(intent);
		finish();
	}

	@OnClick({R.id.tiny, R.id.small, R.id.size_normal, R.id.big, R.id.large})
	public void onSizeClick(View v){
		for(View button : sizeButtons){
			button.setBackground(null);
		}
		size = PrefsHelper.Size.valueOf((String) v.getTag());
		sizeButtons[size.ordinal()].setBackgroundResource(R.drawable.selection_bar);
		exampleView.getHexDrawer().setTileSize(size.getDp(getResources()));
	}

	@OnClick({R.id.blue, R.id.green, R.id.purple, R.id.spectrum})
	public void onColourClick(View v){
		for(View button : colourButtons){
			button.setBackground(null);
		}
		colour = Colour.valueOf((String) v.getTag());
		colourButtons[colour.ordinal()].setBackgroundResource(R.drawable.selection_bar);
		exampleView.getHexDrawer().setColour(colour);
	}

	@Override
	public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
		exampleView.getHexDrawer().setSpeed((500 * seekBar.getMax() + MAX_SPEED_OFFSET) - (progress * 500));
	}

	@Override
	public void onStartTrackingTouch(final SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(final SeekBar seekBar) {

	}
}
