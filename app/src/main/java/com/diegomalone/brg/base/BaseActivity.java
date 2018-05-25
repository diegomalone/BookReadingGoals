package com.diegomalone.brg.base;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.diegomalone.brg.R;
import com.diegomalone.brg.model.Book;
import com.diegomalone.brg.ui.add.book.AddBookActivity;
import com.diegomalone.brg.ui.finished.list.FinishedListActivity;
import com.diegomalone.brg.ui.reading.now.ReadingNowActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final String DATABASE_NAME = "books";

    protected DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private DrawerLayout drawerLayout;

    protected void setupToolbar(Toolbar toolbar) {
        setupToolbar(toolbar, null, null);
    }

    protected void setupToolbar(Toolbar toolbar, DrawerLayout drawerLayout, NavigationView navigationView) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        if (drawerLayout != null && navigationView != null) {
            this.drawerLayout = drawerLayout;

            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                }

                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };

            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();

            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (drawerLayout != null) {
            drawerLayout.closeDrawer(Gravity.START);
        }

        switch (item.getItemId()) {
            case R.id.menuAddBook: {
                Intent intent = new Intent(BaseActivity.this, AddBookActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menuReadingNow: {
                Intent intent = new Intent(BaseActivity.this, ReadingNowActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menuFinishedBooks: {
                Intent intent = new Intent(BaseActivity.this, FinishedListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menuSettings: {
                // TODO
                break;
            }
        }

        return false;
    }

    protected void storeBook(Book book) {
        database.child(DATABASE_NAME).child(book.getUuid()).setValue(book);
    }
}
