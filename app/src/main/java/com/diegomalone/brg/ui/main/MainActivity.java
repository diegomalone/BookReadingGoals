package com.diegomalone.brg.ui.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diegomalone.brg.R;
import com.diegomalone.brg.base.BaseActivity;
import com.diegomalone.brg.model.Book;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar(toolbar, drawerLayout, navigationView);
        configureUI();

        showFakeData();
    }

    private void configureUI() {

    }

    private void showFakeData() {
        Book book = new Book("Dan Brown", "Origin", 427, 140);
        book.setStarted(true);
        book.setStartedDate("05/01/2018");
        book.setDeadline("06/15/2018");

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
    }
}
