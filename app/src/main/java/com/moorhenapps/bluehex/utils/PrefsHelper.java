package com.moorhenapps.bluehex.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.TypedValue;

public class PrefsHelper {

    public static final int MAX_SPEED_OFFSET = 500;

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
    private static final String PREF_SPEED_INT = "speed.int";
    private SharedPreferences preferences;

    public PrefsHelper(Context ctx){
        preferences = ctx.getSharedPreferences("wallpaper", Context.MODE_PRIVATE);
    }

    public void setSize(Size size){
        preferences.edit().putString(PREF_SIZE, size.name()).commit();
    }

    public Size getSize(){
        return Size.valueOf(preferences.getString(PREF_SIZE, Size.NORMAL.name()));
    }


    public void setColour(Colour colour){
        preferences.edit().putString(PREF_COLOUR, colour.name()).commit();
    }

    public Colour getColour(){
        return Colour.valueOf(preferences.getString(PREF_COLOUR, Colour.BLUE.name()));
    }

    public void setSpeed(int speed){
        preferences.edit().putInt(PREF_SPEED_INT, speed).commit();
    }

    public int getSpeed(){
        return preferences.getInt(PREF_SPEED_INT, MAX_SPEED_OFFSET);
    }
}
