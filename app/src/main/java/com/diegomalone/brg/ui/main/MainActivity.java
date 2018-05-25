package com.diegomalone.brg.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import com.diegomalone.brg.ui.add.book.AddBookActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.contentContainer)
    ViewGroup contentContainer;

    @BindView(R.id.emptyStateContainer)
    ViewGroup emptyContainer;

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

    @BindView(R.id.deadlineValueTextView)
    TextView deadlineValueTextView;

    @BindView(R.id.currentPaceValueTextView)
    TextView currentPaceTextView;

    @BindView(R.id.requiredPaceValueTextView)
    TextView requiredPaceTextView;

    @BindView(R.id.updateProgressFab)
    FloatingActionButton updateProgressFAB;

    @BindView(R.id.addBookFab)
    FloatingActionButton addBookFAB;

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar(toolbar, drawerLayout, navigationView);
        configureUI();

        loadDatabaseBooks();
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
        if (isEmpty) {
            contentContainer.setVisibility(GONE);
            emptyContainer.setVisibility(VISIBLE);
        } else {
            contentContainer.setVisibility(VISIBLE);
            emptyContainer.setVisibility(GONE);
        }
    }

    private void openUpdateProgressDialog() {
        View updateProgressDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_progress, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(updateProgressDialogView);

        final EditText currentPageEditText = updateProgressDialogView.findViewById(R.id.currentPageEditText);
        final TextView totalPagesTextView = updateProgressDialogView.findViewById(R.id.totalPagesTextView);

        totalPagesTextView.setText(getString(R.string.main_screen_update_progress_dialog_page_pattern, book.getTotalPages()));
        currentPageEditText.setText(String.valueOf(book.getCurrentPage()));

        alertDialogBuilder
                .setCancelable(true)
                .setTitle(R.string.main_screen_update_progress_dialog_title)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // TODO Update Page (currentPageEditText.getText())
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void loadDatabaseBooks() {
        database.keepSynced(true);

        database.child(DATABASE_NAME).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Book> books = new ArrayList<>();

                for (DataSnapshot bookDataSnapshot : dataSnapshot.getChildren()) {
                    Book book = bookDataSnapshot.getValue(Book.class);
                    books.add(book);
                }

                for (Book book : books) {
                    if (book.isDefault()) {
                        showBook(book);
                        return;
                    }
                }

                if (!books.isEmpty()) {
                    showBook(books.get(books.size() - 1));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.w(databaseError.toException(), "Error getting data from database");
            }
        });
    }

    private void showBook(Book book) {
        this.book = book;

        authorNameTextView.setText(book.getAuthorName());
        bookTitleTextView.setText(book.getTitle());
        currentPageValueTextView.setText(String.valueOf(book.getCurrentPage()));
        totalPagesValueTextView.setText(String.valueOf(book.getTotalPages()));

        readingProgressBar.setMax(book.getTotalPages());
        readingProgressBar.setProgress(book.getCurrentPage());

        deadlineValueTextView.setText(book.getDeadline());

        String fakePace = "12";
        currentPaceTextView.setText(getString(R.string.main_screen_pace_value_pattern, fakePace));
        requiredPaceTextView.setText(getString(R.string.main_screen_pace_value_pattern, fakePace));

        showEmptyState(false);
    }
}
