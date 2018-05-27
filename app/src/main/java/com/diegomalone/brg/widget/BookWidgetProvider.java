package com.diegomalone.brg.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.diegomalone.brg.R;
import com.diegomalone.brg.model.Book;
import com.diegomalone.brg.ui.add.book.AddBookActivity;
import com.diegomalone.brg.ui.main.MainActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BookWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Book book) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_book);

        PendingIntent pendingIntent;

        if (book != null) {
            views.setTextViewText(R.id.bookTitleTextView, book.getTitle());
            views.setTextViewText(R.id.authorNameTextView, book.getAuthorName());

            views.setViewVisibility(R.id.readingProgressBar, VISIBLE);
            views.setViewVisibility(R.id.pagesTextView, VISIBLE);

            views.setProgressBar(R.id.readingProgressBar, book.getTotalPages(), book.getCurrentPage(), false);
            views.setTextViewText(R.id.pagesTextView, context.getString(R.string.widget_pages_pattern,
                    book.getCurrentPage(), book.getTotalPages()));

            Intent intent = new Intent(context, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        } else {
            views.setTextViewText(R.id.bookTitleTextView, context.getString(R.string.widget_no_book_set_title));
            views.setTextViewText(R.id.authorNameTextView, context.getString(R.string.widget_no_book_set_subtitle));

            views.setViewVisibility(R.id.readingProgressBar, GONE);
            views.setViewVisibility(R.id.pagesTextView, GONE);

            Intent intent = new Intent(context, AddBookActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        }

        views.setOnClickPendingIntent(R.id.widgetViewGroup, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        BookWidgetManager bookWidgetManager = new BookWidgetManager(context);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, bookWidgetManager.getBook());
        }
    }
}
