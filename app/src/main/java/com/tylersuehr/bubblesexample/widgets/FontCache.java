package com.tylersuehr.bubblesexample.widgets;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.StringDef;
import java.util.HashMap;
import java.util.Map;
/**
 * Copyright 2017 Tyler Suehr
 * Created by tyler on 6/9/2017.
 */
final class FontCache {
    static final String DEFAULT = "OpenSans-Regular.ttf";
    static final String MEDIUM = "OpenSans-Semibold.ttf";
    static final String BOLD = "OpenSans-Bold.ttf";
    static final String LIGHT = "OpenSans-Light.ttf";
    static final String ALTERNATE = "FiraSans-Regular.ttf";
    private static Map<String, Typeface> cache;


    /**
     * Loads or creates a custom font from the assets directory.
     * @param c {@link Context}
     * @param fontType {@link FontCache.Font}
     * @return {@link Typeface}
     */
    static Typeface create(Context c, @FontCache.Font String fontType) {
        final AssetManager assets = c.getAssets();
        if (cache == null) {
            cache = new HashMap<>();
        }
        Typeface tf = cache.get(fontType);
        if (tf == null) {
            tf = Typeface.createFromAsset(assets, fontType);
            cache.put(fontType, tf);
        }
        return tf;
    }

    /**
     * Loads or creates a custom font using an enum value from an attribute.
     * @param c {@link Context}
     * @param value Attribute enum value
     * @return {@link Typeface}
     */
    static Typeface create(Context c, int value) {
        String fontName;
        switch (value) {
            case 0:
            default:fontName = DEFAULT; break;
            case 1:fontName = LIGHT;break;
            case 2:fontName = MEDIUM;break;
            case 3:fontName = BOLD;break;
            case 4:fontName = ALTERNATE;break;
        }
        return create(c, fontName);
    }

    @StringDef(value = { DEFAULT,MEDIUM,BOLD,LIGHT,ALTERNATE })
    @interface Font {}
}