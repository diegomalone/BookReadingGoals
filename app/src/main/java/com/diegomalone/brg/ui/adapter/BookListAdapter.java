package com.diegomalone.brg.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.diegomalone.brg.R;
import com.diegomalone.brg.model.Book;
import com.diegomalone.brg.ui.events.BookCardSetDefaultMenuClickedListener;
import com.diegomalone.brg.view.BookCardView;

import java.util.ArrayList;
import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Book> bookList = new ArrayList<>();

    private BookCardSetDefaultMenuClickedListener bookCardSetDefaultMenuClickedListener;

    public BookListAdapter(Context context, List<Book> bookList,
                           BookCardSetDefaultMenuClickedListener bookCardSetDefaultMenuClickedListener) {
        this.context = context;
        updateList(bookList);

        this.bookCardSetDefaultMenuClickedListener = bookCardSetDefaultMenuClickedListener;
    }

    public void updateList(List<Book> bookList) {
        this.bookList.clear();

        if (bookList != null) {
            this.bookList.addAll(bookList);
        }
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookCardView bookCardView = new BookCardView(context);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bookCardView.setLayoutParams(layoutParams);

        return new ViewHolder(bookCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Book book = bookList.get(position);

        holder.bookCardView.setBook(book);

        holder.bookCardView.moreOptionsIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.bookCardView.moreOptionsIconImageView, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        BookCardView bookCardView;

        ViewHolder(View itemView) {
            super(itemView);

            bookCardView = (BookCardView) itemView;
        }
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.book_card_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new CustomMenuItemClickListener(position));
        popup.show();
    }

    class CustomMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;

        public CustomMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.menuSetDefault:
                    if (bookCardSetDefaultMenuClickedListener != null) {
                        bookCardSetDefaultMenuClickedListener.onSetDefaultClicked(position);
                    }

                    return true;
                default:
            }

            return false;
        }
    }
}

