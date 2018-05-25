package com.diegomalone.brg.ui.finished.list;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.diegomalone.brg.R;
import com.diegomalone.brg.base.BaseActivity;
import com.diegomalone.brg.model.Book;
import com.diegomalone.brg.ui.adapter.BookListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FinishedListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bookListRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyStateContainer)
    ViewGroup emptyStateContainer;

    private BookListAdapter bookListAdapter;

    private ArrayList<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_list);
        ButterKnife.bind(this);

        setupToolbar(toolbar);
        configureUI();

        loadDatabaseBooks();
    }

    private void configureUI() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        bookListAdapter = new BookListAdapter(this, null, null);
        recyclerView.setAdapter(bookListAdapter);
    }

    private void showEmptyState(boolean isEmpty) {
        if (isEmpty) {
            recyclerView.setVisibility(GONE);
            emptyStateContainer.setVisibility(VISIBLE);
        } else {
            recyclerView.setVisibility(VISIBLE);
            emptyStateContainer.setVisibility(GONE);
        }
    }

    @Override
    protected void bookListLoaded(List<Book> bookList) {
        List<Book> readingNowBookList = new ArrayList<>();

        for (Book book : bookList) {
            if (book.isFinished()) {
                readingNowBookList.add(book);
            }
        }

        showBookList(readingNowBookList);
    }

    private void showBookList(List<Book> bookList) {
        if (bookList == null || bookList.isEmpty()) return;

        this.bookList.clear();
        this.bookList.addAll(bookList);

        if (bookListAdapter != null) {
            bookListAdapter.updateList(bookList);
            bookListAdapter.notifyDataSetChanged();
        }

        showEmptyState(false);
    }
}
