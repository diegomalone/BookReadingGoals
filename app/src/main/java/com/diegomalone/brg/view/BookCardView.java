package com.diegomalone.brg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diegomalone.brg.R;
import com.diegomalone.brg.model.Book;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookCardView extends LinearLayout {

    @BindView(R.id.defaultIconImageView)
    ImageView defaultIconImageView;

    @BindView(R.id.moreOptionsIconImageView)
    public ImageView moreOptionsIconImageView;

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

        startedDateValueTextView.setText(book.getStartedDate());

        readingProgressBar.setMax(book.getTotalPages());
        readingProgressBar.setProgress(book.getCurrentPage());

        defaultIconImageView.setVisibility(GONE);
        moreOptionsIconImageView.setVisibility(GONE);

        finishedDateLabelTextView.setVisibility(GONE);
        finishedDateValueTextView.setVisibility(GONE);

        deadlineLabelTextView.setVisibility(GONE);
        deadlineValueTextView.setVisibility(GONE);

        if (!book.isFinished()) {
            deadlineLabelTextView.setVisibility(VISIBLE);
            deadlineValueTextView.setVisibility(VISIBLE);

            String deadlineValue = book.getDeadline();
            if (deadlineValue == null || deadlineValue.isEmpty()) {
                deadlineValue = getContext().getResources().getString(R.string.deadline_not_set);
            }

            deadlineValueTextView.setText(deadlineValue);

            if (book.isDefault()) {
                defaultIconImageView.setVisibility(VISIBLE);
            } else {
                moreOptionsIconImageView.setVisibility(VISIBLE);
            }

            pagesTextView.setText(getContext().getString(R.string.view_book_card_pages_pattern,
                    book.getCurrentPage(), book.getTotalPages()));
        } else {
            finishedDateLabelTextView.setVisibility(VISIBLE);
            finishedDateValueTextView.setVisibility(VISIBLE);

            finishedDateValueTextView.setText(book.getFinishedDate());

            pagesTextView.setText(getContext().getString(R.string.view_book_card_total_pages_pattern,
                    book.getTotalPages()));
        }
    }

    public Book getBook() {
        return book;
    }
}
