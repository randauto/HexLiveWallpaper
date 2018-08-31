package com.moorhenapps.bluehex.wallpaper;

import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.moorhenapps.bluehex.utils.PrefsHelper;

public class GridWallpaper extends WallpaperService {
	public GridWallpaper() {
	}

	@Override
	public Engine onCreateEngine() {
		return new GridEngine();
	}

	private class GridEngine extends WallpaperService.Engine {
		private final Handler handler = new Handler();
		private final Runnable drawRunner = new Runnable() {
			@Override
			public void run() {
				draw();
			}
		};

		private boolean visible = true;
        private ColouredHexDrawer colouredHexDrawer;

		GridEngine() {
            colouredHexDrawer = new ColouredHexDrawer();
            handler.removeCallbacks(drawRunner);
			handler.post(drawRunner);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			this.visible = visible;
			if (visible) {
                PrefsHelper prefs = new PrefsHelper(GridWallpaper.this);
                colouredHexDrawer.setColour(prefs.getColour());
                colouredHexDrawer.setTileSize(prefs.getSize().dp(getResources()));
                colouredHexDrawer.setSpeed(prefs.getSpeed().getMs());
                handler.removeCallbacks(drawRunner);
				handler.post(drawRunner);
			} else {
				handler.removeCallbacks(drawRunner);
			}
		}
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			this.visible = false;
			handler.removeCallbacks(drawRunner);
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            PrefsHelper prefs = new PrefsHelper(GridWallpaper.this);
            colouredHexDrawer.setColour(prefs.getColour());
            colouredHexDrawer.setTileSize(prefs.getSize().dp(getResources()));
            colouredHexDrawer.setSpeed(prefs.getSpeed().getMs());
            colouredHexDrawer.setCanvasSize(width, height);
		}

		@Override
		public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
			super.onSurfaceRedrawNeeded(holder);
			draw();
		}

		private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    colouredHexDrawer.draw(canvas);
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

            handler.removeCallbacks(drawRunner);

            if (visible) {
                handler.postDelayed(drawRunner, 8);
            }
		}
	}
}