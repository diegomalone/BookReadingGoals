package com.diegomalone.brg.util;

import android.support.annotation.Nullable;

public class NumberUtils {

    @Nullable
    public static Integer getIntegerValue(String text) {
        if (!text.matches("\\d+")) return null;

        return Integer.parseInt(text);
    }
}
