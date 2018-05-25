package com.diegomalone.brg.ui.add.book;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.diegomalone.brg.R;
import com.diegomalone.brg.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AddBookActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.authorNameInputLayout)
    TextInputLayout authorNameInputLayout;

    @BindView(R.id.authorNameEditText)
    EditText authorNameEditText;

    @BindView(R.id.bookTitleInputLayout)
    TextInputLayout bookTitleInputLayout;

    @BindView(R.id.bookTitleEditText)
    EditText bookTitleEditText;

    @BindView(R.id.totalPagesInputLayout)
    TextInputLayout totalPagesInputLayout;

    @BindView(R.id.totalPagesEditText)
    EditText totalPagesEditText;

    @BindView(R.id.setDeadlineCheckbox)
    CheckBox setDeadlineCheckBox;

    @BindView(R.id.deadlineLabelTextView)
    TextView deadlineLabelTextView;

    @BindView(R.id.deadlineValueTextView)
    TextView deadlineValueTextView;

    @BindView(R.id.alreadyStartedCheckbox)
    CheckBox alreadyStartedCheckBox;

    @BindView(R.id.currentPageInputLayout)
    TextInputLayout currentPageInputLayout;

    @BindView(R.id.currentPageEditText)
    EditText currentPageEditText;

    @BindView(R.id.setDefaultCheckbox)
    CheckBox setDefaultCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ButterKnife.bind(this);

        setupToolbar(toolbar);
        configureUI();
    }

    private void configureUI() {
        setDeadlineCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int visibility = isChecked ? VISIBLE : GONE;

                deadlineLabelTextView.setVisibility(visibility);
                deadlineValueTextView.setVisibility(visibility);

                if (isChecked) {
                    // TODO Show date picker
                }
            }
        });

        alreadyStartedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int visibility = isChecked ? VISIBLE : GONE;

                currentPageInputLayout.setVisibility(visibility);
                currentPageEditText.requestFocus();
            }
        });
    }
}
