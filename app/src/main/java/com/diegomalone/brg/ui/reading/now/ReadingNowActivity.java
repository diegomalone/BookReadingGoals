package com.diegomalone.brg.ui.reading.now;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.diegomalone.brg.R;
import com.diegomalone.brg.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadingNowActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_now);
        ButterKnife.bind(this);

        setupToolbar(toolbar);
        configureUI();
    }

    private void configureUI() {

    }
}
