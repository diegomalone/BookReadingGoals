package com.diegomalone.brg.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diegomalone.brg.R;
import com.diegomalone.brg.model.Book;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookCardView extends CardView {

    @BindView(R.id.defaultIconImageView)
    ImageView defaultIconImageView;

    @BindView(R.id.bookTitleTextView)
    TextView bookTitleTextView;

    @BindView(R.id.authorNameTextView)
    TextView authorNameTextView;

    @BindView(R.id.pagesTextView)
    TextView pagesTextView;

    @BindView(R.id.readingProgressBar)
    ProgressBar readingProgressBar;

    @BindView(R.id.startedDateValueTextView)
    TextView startedDateValueTextView;

    @BindView(R.id.finishedDateLabelTextView)
    TextView finishedDateLabelTextView;

    @BindView(R.id.finishedDateValueTextView)
    TextView finishedDateValueTextView;

    @BindView(R.id.deadlineLabelTextView)
    TextView deadlineLabelTextView;

    @BindView(R.id.deadlineValueTextView)
    TextView deadlineValueTextView;

    private Book book;

    public BookCardView(Context context) {
        super(context);
        initialize(context);
    }

    public BookCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        View view = inflate(context, R.layout.view_book_card, this);
        ButterKnife.bind(this, view);
    }

    public void setBook(Book book) {
        this.book = book;

        bookTitleTextView.setText(book.getTitle());
        authorNameTextView.setText(book.getAuthorName());
    }

    public Book getBook() {
        return book;
    }
}
