package com.moorhenapps.bluehex

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.TypedValue
import android.view.View
import android.widget.SeekBar
import com.moorhenapps.bluehex.utils.Colour
import com.moorhenapps.bluehex.utils.PrefsHelper
import com.moorhenapps.bluehex.wallpaper.GridWallpaper
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : Activity() {

    private val sizeButtons by lazy { listOf(size_small, size_medium, size_large) }
    private val speedButtons by lazy { listOf(speed_slow, speed_medium, speed_fast) }
    private val colourButtons by lazy { listOf(colour_blue, colour_green, colour_red, colour_grey, colour_lgbt, colour_dark, colour_pastel, colour_gold, colour_nexus, colour_purple, colour_retro, colour_pink, colour_asex, colour_bisex, colour_nonbinary, colour_trans) }

    private val wallpaperManager by lazy { getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager }

    private lateinit var wallpaperIntent: Intent
    private lateinit var prefsHelper: PrefsHelper

    private val settingsComponent by lazy { ComponentName(this, "$packageName.LauncherSettingsActivity") }

    public override fun onCreate(savedInstanceState: Bundle?) {

        if (resources.getBoolean(R.bool.is_tablet)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setDrawBelowStatusBar()
        positionViewsInsideSystemViews()

        wallpaperIntent = makeSetWallpaperIntent()

        val onSizeClick = View.OnClickListener { view ->
            val size = PrefsHelper.Size.valueOf(view.tag as String)
            prefsHelper.size = size
            view.isSelected = true
        }

        val onColourClick = View.OnClickListener { view ->
            val colour = Colour.valueOf(view.tag as String)
            prefsHelper.colour = colour
            view.isSelected = true
            onColourChange(colour)
        }

        val onSpeedClick = View.OnClickListener { view ->
            val speed = PrefsHelper.Speed.valueOf(view.tag as String)
            prefsHelper.speed = speed
            view.isSelected = true
        }

        sizeButtons.forEach { it.setOnClickListener(onSizeClick) }
        speedButtons.forEach { it.setOnClickListener(onSpeedClick) }
        colourButtons.forEach { it.setOnClickListener(onColourClick) }

        prefsHelper = PrefsHelper(this)

        speed_bar.progress = ((prefsHelper.advSpeed / 10000.0) * 100).toInt()
        size_bar.progress = (((prefsHelper.advSize - 10) / 25.0) * 100).toInt()

        speed_bar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val percent = Math.max(progress,1) / 100.0
                    val value = percent * 10000.0
                    prefsHelper.advSpeed = value.toInt()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        size_bar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val percent = Math.max(progress,1) / 100.0
                    val value = (percent * 25.0) + 10
                    prefsHelper.advSize = value.toInt()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        set_wallpaper.setOnClickListener {
            startActivityForResult(wallpaperIntent, REQUEST_SET_WALLPAPER)
        }

        showControls()

        settings_toggle.setOnClickListener {
            prefsHelper.advancedSettings = !prefsHelper.advancedSettings
            showControls()
        }
    }

    private fun showControls() {
        if (prefsHelper.advancedSettings) {
            advanced.visibility = View.VISIBLE
            buttons.visibility = View.GONE
        } else {
            advanced.visibility = View.GONE
            buttons.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()

        val colour = prefsHelper.colour
        val speed = prefsHelper.speed
        val size = prefsHelper.size

        colourButtons.first { it.tag == colour.name }.isSelected = true
        sizeButtons.first { it.tag == size.name }.isSelected = true
        speedButtons.first { it.tag == speed.name }.isSelected = true

        onColourChange(colour)

        if (isWallpaperSupported()) {
            set_wallpaper.visibility = if (isThisAppTheSelectedWallpaper()) View.GONE else View.VISIBLE
        }

        show_app_icon.setOnCheckedChangeListener(null)
        show_app_icon.isChecked = packageManager.getComponentEnabledSetting(settingsComponent) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED ||  packageManager.getComponentEnabledSetting(settingsComponent) == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
        show_app_icon.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                packageManager.setComponentEnabledSetting(settingsComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
            } else {
                packageManager.setComponentEnabledSetting(settingsComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SET_WALLPAPER) {
            if (isThisAppTheSelectedWallpaper()) {
                finish()
            }
        }
    }

    private fun onColourChange(colour: Colour) {
        (sizeButtons + speedButtons).forEach { it.accentColour = colour.colours[0] }
        set_wallpaper.setTextColor(colour.colours[0])
        show_app_icon.setTextColor(colour.colours[0])

        if (colour.useLightIcons) {
            setLightStatusBarIcons()
        } else {
            setDarkStatusBarIcons()
        }
    }

    private fun isThisAppTheSelectedWallpaper(): Boolean {
        val info = wallpaperManager.wallpaperInfo
        return info != null && info.component == ComponentName(this, GridWallpaper::class.java)
    }

    private fun isWallpaperSupported(): Boolean {
        return if (intent.resolveActivity(packageManager) != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.isWallpaperSupported && wallpaperManager.isSetWallpaperAllowed
            } else {
                true
            }
        } else {
            false
        }
    }

    private fun setDrawBelowStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    private fun positionViewsInsideSystemViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.setOnApplyWindowInsetsListener { _, insets ->
                run {
                    val params = set_wallpaper.layoutParams
                            ?: ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                    (params as ConstraintLayout.LayoutParams).topMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt() + insets.systemWindowInsetTop
                    set_wallpaper.layoutParams = params
                }
                run {
                    val params = palette.layoutParams
                            ?: ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                    (params as ConstraintLayout.LayoutParams).bottomMargin = insets.systemWindowInsetBottom
                    palette.layoutParams = params
                }
                insets.consumeSystemWindowInsets()
            }
        }
    }

    private fun setDarkStatusBarIcons() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.decorView.systemUiVisibility = flags
        }
    }

    private fun setLightStatusBarIcons() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = 0
        }
    }

    private fun makeSetWallpaperIntent(): Intent {
        return Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
            putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, ComponentName(this@SettingsActivity, GridWallpaper::class.java))
        }
    }

    companion object {
        private const val REQUEST_SET_WALLPAPER = 100
    }
}
