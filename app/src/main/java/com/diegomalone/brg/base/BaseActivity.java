package com.diegomalone.brg.base;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.diegomalone.brg.R;

import butterknife.BindView;

public abstract class BaseActivity extends AppCompatActivity {

    protected void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }
}
