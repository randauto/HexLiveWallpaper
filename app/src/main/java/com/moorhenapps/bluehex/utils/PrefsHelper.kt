package com.moorhenapps.bluehex.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.TypedValue

class PrefsHelper(ctx: Context) {
    val preferences: SharedPreferences = ctx.getSharedPreferences("wallpaper", Context.MODE_PRIVATE)

    enum class Size constructor(private val size: Int) {
        SMALL(10), MEDIUM(14), LARGE(18);

        fun dp(resources: Resources): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size.toFloat(), resources.displayMetrics).toInt()
        }
    }

    enum class Speed constructor(val ms: Int) {
        SLOW(3000), MEDIUM(2000), FAST(1000);
    }

    var speed: Speed
        get() = Speed.valueOf(preferences.getString(PREF_SPEED, "SLOW")!!)
        set(value) {
            preferences.edit().putString(PREF_SPEED, value.name).apply()
        }

    var size: Size
        get() = Size.valueOf(preferences.getString(PREF_SIZE, "LARGE")!!)
        set(value) {
            preferences.edit().putString(PREF_SIZE, value.name).apply()
        }

    var colour: Colour
        get() = Colour.valueOf(preferences.getString(PREF_COLOUR, "BLUE")!!)
        set(value) {
            preferences.edit().putString(PREF_COLOUR, value.name).apply()
        }

    var advSpeed: Int
        get() = preferences.getInt(PREF_ADV_SPEED, 3000)
        set(value) {
            preferences.edit().putInt(PREF_ADV_SPEED, value).apply()
        }

    var advSize: Int
        get() = preferences.getInt(PREF_ADV_SIZE, 16)
        set(value) {
            preferences.edit().putInt(PREF_ADV_SIZE, value).apply()
        }

    var advancedSettings: Boolean
        get() = preferences.getBoolean(PREF_ADVANCED, false)
        set(value) {
            preferences.edit().putBoolean(PREF_ADVANCED, value).apply()
        }

    companion object {
        private const val PREF_SIZE = "new_size.enum"
        private const val PREF_COLOUR = "new_colour.enum"
        private const val PREF_SPEED = "new_speed.enum"
        private const val PREF_ADVANCED = "advanced. bool"
        private const val PREF_ADV_SPEED = "adv_speed.int"
        private const val PREF_ADV_SIZE = "adv_size.int"
    }
}
