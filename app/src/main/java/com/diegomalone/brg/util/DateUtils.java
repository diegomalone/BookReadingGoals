package com.diegomalone.brg.util;

import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class DateUtils {

    private static final String DATE_FORMAT = "MM/dd/yyyy";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    @Nullable
    public static String getDateAsString(Date date) {
        if (date == null) return null;

        return dateFormat.format(date);
    }

    @Nullable
    public static Date getDateFromString(String dateAsString) {
        if (dateAsString == null) return null;

        try {
            return dateFormat.parse(dateAsString);
        } catch (ParseException e) {
            Timber.e(e, "Failed to parse date");
        }

        return null;
    }

    public static int getDaysBetweenDates(Date endDate, Date startedDate) {
        long diffInMillis = (endDate.getTime() - startedDate.getTime());
        return (int) (diffInMillis / (1000 * 60 * 60 * 24)) + 1;
    }
}
