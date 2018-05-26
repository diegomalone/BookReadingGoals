package com.diegomalone.brg.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.diegomalone.brg.R;
import com.diegomalone.brg.model.Book;
import com.google.gson.Gson;

public class BookWidgetManager {

    private static final String PREFS_FILE_NAME = "book_reading_goals";
    private static final String KEY_WIDGET_BOOK_ID = "bookId";

    private Context context;
    private SharedPreferences sharedPreferences;

    public BookWidgetManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setBook(Book book) {
        String bookJson = new Gson().toJson(book);

        sharedPreferences.edit().putString(KEY_WIDGET_BOOK_ID, bookJson).apply();

        updateWidget();
    }

    public Book getBook() {
        String bookJson = sharedPreferences.getString(KEY_WIDGET_BOOK_ID, null);
        if (bookJson == null) return null;

        return new Gson().fromJson(bookJson, Book.class);
    }

    private void updateWidget() {
        Intent intent = new Intent(context, BookWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, BookWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        context.sendBroadcast(intent);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetViewGroup);
    }
}
