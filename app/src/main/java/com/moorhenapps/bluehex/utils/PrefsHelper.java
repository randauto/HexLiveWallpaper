package com.moorhenapps.bluehex.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.TypedValue;

public class PrefsHelper {

    private static final int SPEED_INCREMENT = 100;
    private static final int DEFAULT_SPEED = 1;

    public enum Size {
        TINY(6), SMALL(10), NORMAL(12), BIG(14), LARGE(18);

        private int size;

        Size(int size) {
            this.size = size;
        }

        public int getDp(Resources resources){
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, resources.getDisplayMetrics());
        }
    }

    private static final String PREF_SIZE = "size.enum";
    private static final String PREF_COLOUR = "colour.enum";
    private static final String PREF_SPEED_INT = "speed2.int";
    private static final String PREF_SPEED_SETTING_INT = "speed_setting.int";
    private SharedPreferences preferences;

    public PrefsHelper(Context ctx){
        preferences = ctx.getSharedPreferences("wallpaper", Context.MODE_PRIVATE);
    }

    public void setSize(Size size){
        preferences.edit().putString(PREF_SIZE, size.name()).apply();
    }

    public Size getSize(){
        return Size.valueOf(preferences.getString(PREF_SIZE, Size.NORMAL.name()));
    }

    public void setColour(Colour colour){
        preferences.edit().putString(PREF_COLOUR, colour.name()).apply();
    }

    public Colour getColour(){
        return Colour.valueOf(preferences.getString(PREF_COLOUR, Colour.BLUE.name()));
    }

    public void setSpeedSetting(int speedSetting){
        preferences.edit()
                .putInt(PREF_SPEED_SETTING_INT, speedSetting)
                .putInt(PREF_SPEED_INT, speedSetting * SPEED_INCREMENT)
                .apply();
    }

    public int getSpeedSetting(){
        return preferences.getInt(PREF_SPEED_SETTING_INT, DEFAULT_SPEED);
    }

    public int getSpeed(){
        return preferences.getInt(PREF_SPEED_INT, SPEED_INCREMENT);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }
}
