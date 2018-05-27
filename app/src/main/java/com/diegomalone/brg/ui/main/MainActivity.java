package com.diegomalone.brg.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diegomalone.brg.R;
import com.diegomalone.brg.base.BaseActivity;
import com.diegomalone.brg.model.Book;
import com.diegomalone.brg.service.UpdateProgressIntentService;
import com.diegomalone.brg.ui.add.book.AddBookActivity;
import com.diegomalone.brg.util.DateUtils;
import com.diegomalone.brg.widget.BookWidgetManager;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.diegomalone.brg.analytics.AnalyticsValues.MAIN_ACTIVITY_ID;
import static com.diegomalone.brg.analytics.AnalyticsValues.READING_PROGRESS_UPDATED;
import static com.diegomalone.brg.analytics.AnalyticsValues.SCREEN_OPEN;
import static com.diegomalone.brg.service.UpdateProgressIntentService.REQUEST_STATUS_ERROR;
import static com.diegomalone.brg.service.UpdateProgressIntentService.REQUEST_STATUS_INTERNET_ERROR;
import static com.diegomalone.brg.service.UpdateProgressIntentService.REQUEST_STATUS_SUCCESS;
import static com.diegomalone.brg.util.NumberUtils.getIntegerValue;

public class MainActivity extends BaseActivity {

    public static final String BOOK_EXTRA = "book";
    public static final String DIALOG_OPEN_EXTRA = "dialogOpen";
    public static final String DIALOG_CURRENT_PAGE_EXTRA = "dialogCurrentPage";

    public static final int DIALOG_OPEN_EXTRA_TRUE = 1;
    public static final int DIALOG_OPEN_EXTRA_FALSE = 2;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.contentContainer)
    ViewGroup contentContainer;

    @BindView(R.id.emptyStateContainer)
    ViewGroup emptyContainer;

    @BindView(R.id.coordinatorLayout)
    ViewGroup coordinatorLayout;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigationView)
    NavigationView navigationView;

    @BindView(R.id.authorNameTextView)
    TextView authorNameTextView;

    @BindView(R.id.bookTitleTextView)
    TextView bookTitleTextView;

    @BindView(R.id.currentPageValueTextView)
    TextView currentPageValueTextView;

    @BindView(R.id.pagesValueTextView)
    TextView totalPagesValueTextView;

    @BindView(R.id.readingProgressBar)
    ProgressBar readingProgressBar;

    @BindView(R.id.startedDateValueTextView)
    TextView startedDateValueTextView;

    @BindView(R.id.deadlineValueTextView)
    TextView deadlineValueTextView;

    @BindView(R.id.currentPaceLabelTextView)
    TextView currentPaceLabelTextView;

    @BindView(R.id.currentPaceValueTextView)
    TextView currentPaceTextView;

    @BindView(R.id.requiredPaceValueTextView)
    TextView requiredPaceTextView;

    @BindView(R.id.requiredPaceLabelTextView)
    TextView requiredPaceLabelTextView;

    @BindView(R.id.updateProgressFab)
    FloatingActionButton updateProgressFAB;

    @BindView(R.id.addBookFab)
    FloatingActionButton addBookFAB;

    private Book book;
    private AlertDialog updateReadingProgressAlertDialog;
    private EditText updateReadingProgressDialogCurrentPageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar(toolbar, drawerLayout, navigationView);
        configureUI();

        if (savedInstanceState != null) {
            setCurrentBook((Book) savedInstanceState.getParcelable(BOOK_EXTRA));

            if (savedInstanceState.getInt(DIALOG_OPEN_EXTRA) == DIALOG_OPEN_EXTRA_TRUE) {
                openUpdateProgressDialog();

                if (updateReadingProgressDialogCurrentPageEditText != null) {
                    updateReadingProgressDialogCurrentPageEditText.setText(savedInstanceState.getString(DIALOG_CURRENT_PAGE_EXTRA));
                }
            }
        }

        loadDatabaseBooks();

        analyticsManager.logContentEvent(MAIN_ACTIVITY_ID, SCREEN_OPEN);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BOOK_EXTRA, getCurrentBook());
        outState.putInt(DIALOG_OPEN_EXTRA,
                updateReadingProgressAlertDialog.isShowing() ? DIALOG_OPEN_EXTRA_TRUE : DIALOG_OPEN_EXTRA_FALSE);

        if (updateReadingProgressDialogCurrentPageEditText != null) {
            outState.putString(DIALOG_CURRENT_PAGE_EXTRA, updateReadingProgressDialogCurrentPageEditText.getText().toString().trim());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UpdateProgressIntentService.BROADCAST_ACTION);

        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    private void configureUI() {
        updateProgressFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateProgressDialog();
            }
        });

        addBookFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showEmptyState(boolean isEmpty) {
        addBookFAB.setVisibility(GONE);
        updateProgressFAB.setVisibility(GONE);

        if (isEmpty) {
            contentContainer.setVisibility(GONE);
            emptyContainer.setVisibility(VISIBLE);
            addBookFAB.setVisibility(VISIBLE);
        } else {
            contentContainer.setVisibility(VISIBLE);
            emptyContainer.setVisibility(GONE);
            updateProgressFAB.setVisibility(VISIBLE);
        }
    }

    private void openUpdateProgressDialog() {
        View updateProgressDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_progress, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(updateProgressDialogView);

        updateReadingProgressDialogCurrentPageEditText = updateProgressDialogView.findViewById(R.id.currentPageEditText);
        final TextView totalPagesTextView = updateProgressDialogView.findViewById(R.id.totalPagesTextView);
        final TextView alreadyFinishedTextView = updateProgressDialogView.findViewById(R.id.alreadyFinishedActionTextView);

        totalPagesTextView.setText(getString(R.string.main_screen_update_progress_dialog_page_pattern, book.getTotalPages()));
        updateReadingProgressDialogCurrentPageEditText.setText(String.valueOf(book.getCurrentPage()));

        alertDialogBuilder
                .setCancelable(true)
                .setTitle(R.string.main_screen_update_progress_dialog_title)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing, it will be overridden
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        updateReadingProgressAlertDialog = alertDialogBuilder.create();
        updateReadingProgressAlertDialog.show();

        updateReadingProgressAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer newCurrentPage = getIntegerValue(updateReadingProgressDialogCurrentPageEditText.getText().toString().trim());

                if (newCurrentPage == null) {
                    Snackbar.make(coordinatorLayout, R.string.main_screen_update_progress_dialog_current_page_required, Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (newCurrentPage > book.getTotalPages()) {
                    Snackbar.make(coordinatorLayout, R.string.main_screen_update_progress_dialog_current_page_invalid, Snackbar.LENGTH_LONG).show();
                    return;
                }

                book.setCurrentPage(newCurrentPage);
                storeBook(book);

                analyticsManager.logContentEvent(MAIN_ACTIVITY_ID, READING_PROGRESS_UPDATED);

                sendUpdatedProgress(book);

                updateReadingProgressAlertDialog.dismiss();
            }
        });

        alreadyFinishedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book.setCurrentPage(book.getTotalPages());
                book.setFinished(true);
                book.setDefault(false);
                book.setFinishedDate(DateUtils.getDateAsString(new Date()));

                storeBook(book);
                Snackbar.make(coordinatorLayout, R.string.main_screen_update_progress_dialog_book_finished_success, Snackbar.LENGTH_LONG).show();

                updateReadingProgressAlertDialog.dismiss();
            }
        });
    }

    @Override
    protected void bookListLoaded(List<Book> bookList) {
        for (Book book : bookList) {
            if (book.isDefault() && !book.isFinished()) {
                setCurrentBook(book);
                return;
            }
        }

        // No default book, set one
        for (Book book : bookList) {
            if (!book.isFinished()) {
                book.setDefault(true);
                storeBook(book);
                return;
            }
        }

        // No book reading now
        showEmptyState(true);
        BookWidgetManager bookWidgetManager = new BookWidgetManager(this);
        bookWidgetManager.setBook(null);
    }

    private void showBook(Book book) {
        authorNameTextView.setText(book.getAuthorName());
        bookTitleTextView.setText(book.getTitle());
        currentPageValueTextView.setText(String.valueOf(book.getCurrentPage()));
        totalPagesValueTextView.setText(String.valueOf(book.getTotalPages()));

        readingProgressBar.setMax(book.getTotalPages());
        readingProgressBar.setProgress(book.getCurrentPage());

        startedDateValueTextView.setText(book.getStartedDate());

        if (book.getDeadline() != null && !book.getDeadline().isEmpty()) {
            deadlineValueTextView.setText(book.getDeadline());
        } else {
            deadlineValueTextView.setText(R.string.deadline_not_set);
        }

        updateReadingPace(book);

        showEmptyState(false);
    }

    private void updateReadingPace(Book book) {
        Date startedDate = DateUtils.getDateFromString(book.getStartedDate());

        if (startedDate != null) {
            int totalDaysReading = DateUtils.getDaysBetweenDates(new Date(), startedDate);

            if (totalDaysReading == 0) {
                totalDaysReading = 1;
            }

            int readingPace = book.getCurrentPage() / totalDaysReading;

            currentPaceLabelTextView.setVisibility(VISIBLE);
            currentPaceTextView.setVisibility(VISIBLE);

            currentPaceTextView.setText(getString(R.string.main_screen_pace_value_pattern, readingPace));
        } else {
            currentPaceTextView.setVisibility(GONE);
            currentPaceLabelTextView.setVisibility(GONE);
        }

        Date deadline = DateUtils.getDateFromString(book.getDeadline());

        if (deadline != null) {

            int totalDaysToRead = DateUtils.getDaysBetweenDates(deadline, new Date());
            int pagesToRead = book.getTotalPages() - book.getCurrentPage();

            requiredPaceTextView.setVisibility(VISIBLE);
            requiredPaceLabelTextView.setVisibility(VISIBLE);

            if (totalDaysToRead <= 0) {
                requiredPaceTextView.setText(getString(R.string.main_screen_required_pace_deadline_not_met));
            } else {
                int requiredReadingPace = pagesToRead / totalDaysToRead;

                requiredPaceTextView.setText(getString(R.string.main_screen_pace_value_pattern, requiredReadingPace));
            }
        } else {
            requiredPaceTextView.setVisibility(GONE);
            requiredPaceLabelTextView.setVisibility(GONE);
        }
    }

    private void sendUpdatedProgress(Book book) {
        Intent updateProgressIntent = new Intent(this, UpdateProgressIntentService.class);
        updateProgressIntent.putExtra(UpdateProgressIntentService.INTENT_DATA_BOOK, book);

        startService(updateProgressIntent);
    }

    private Book getCurrentBook() {
        return book;
    }

    private void setCurrentBook(Book book) {
        this.book = book;
        showBook(book);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(UpdateProgressIntentService.REQUEST_STATUS, -1);

            String errorMessage;

            switch (status) {
                case REQUEST_STATUS_SUCCESS: {
                    Snackbar.make(coordinatorLayout, R.string.main_screen_update_progress_dialog_current_page_update_success, Snackbar.LENGTH_LONG).show();
                    return;
                }
                case REQUEST_STATUS_INTERNET_ERROR: {
                    errorMessage = getString(R.string.main_screen_update_progress_send_update_error_internet);
                    break;
                }
                case REQUEST_STATUS_ERROR: {
                    errorMessage = getString(R.string.main_screen_update_progress_send_update_error_server);
                    break;
                }
                default: {
                    errorMessage = getString(R.string.error_unexpected);
                }
            }

            Snackbar snackbar = Snackbar.make(coordinatorLayout, errorMessage, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.try_again, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendUpdatedProgress(getCurrentBook());
                }
            });
            snackbar.show();
        }
    };
}
