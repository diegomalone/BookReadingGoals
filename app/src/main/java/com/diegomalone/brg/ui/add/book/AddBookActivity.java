package com.diegomalone.brg.ui.add.book;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.diegomalone.brg.R;
import com.diegomalone.brg.base.BaseActivity;
import com.diegomalone.brg.model.Book;
import com.diegomalone.brg.util.DateUtils;
import com.mobsandgeeks.saripaar.QuickRule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.adapter.ViewDataAdapter;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.exception.ConversionException;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.diegomalone.brg.util.NumberUtils.getIntegerValue;

@SuppressWarnings("deprecation")
public class AddBookActivity extends BaseActivity implements Validator.ValidationListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.authorNameInputLayout)
    @NotEmpty
    TextInputLayout authorNameInputLayout;

    @BindView(R.id.authorNameEditText)
    EditText authorNameEditText;

    @BindView(R.id.bookTitleInputLayout)
    @NotEmpty
    TextInputLayout bookTitleInputLayout;

    @BindView(R.id.bookTitleEditText)
    EditText bookTitleEditText;

    @BindView(R.id.totalPagesInputLayout)
    @NotEmpty
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

    @BindView(R.id.finishButton)
    Button finishButton;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ButterKnife.bind(this);

        setupToolbar(toolbar);
        configureUI();

        setupValidator();
    }

    private void setupValidator() {
        validator = new Validator(this);
        validator.setValidationListener(this);

        validator.registerAdapter(TextInputLayout.class,
                new ViewDataAdapter<TextInputLayout, String>() {
                    @Override
                    public String getData(TextInputLayout textInputLayout) throws ConversionException {
                        EditText editText = textInputLayout.getEditText();
                        return editText != null ? editText.getText().toString() : null;
                    }
                }
        );

        validator.setViewValidatedAction(new Validator.ViewValidatedAction() {
            @Override
            public void onAllRulesPassed(View view) {
                if (view instanceof TextInputLayout) {
                    ((TextInputLayout) view).setError(null);
                    ((TextInputLayout) view).setErrorEnabled(false);
                }
            }
        });

        validator.put(currentPageInputLayout, new CurrentPageValidationRule());
    }

    private void configureUI() {
        setDeadlineCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int visibility = isChecked ? VISIBLE : GONE;

                deadlineLabelTextView.setVisibility(visibility);
                deadlineValueTextView.setVisibility(visibility);

                if (isChecked) {
                    showDatePickerDialog();
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

        finishButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        deadlineValueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerDialogFragment();
        newFragment.show(getFragmentManager(), "DATE_PICKER");
    }

    @Override
    public void onValidationSucceeded() {
        Integer totalPages = getIntegerValue(totalPagesEditText.getText().toString().trim());
        Integer currentPage = getIntegerValue(currentPageEditText.getText().toString().trim());

        if (totalPages == null) {
            totalPages = 0;
        }

        if (currentPage == null) {
            currentPage = 0;
        }

        Book createdBook = new Book(authorNameEditText.getText().toString().trim(),
                bookTitleEditText.getText().toString().trim(),
                totalPages, currentPage);

        if (setDeadlineCheckBox.isChecked()) {
            createdBook.setDeadline(deadlineValueTextView.getText().toString().trim());
        }

        storeBook(createdBook);

        // TODO Show success message
        finish();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof TextInputLayout) {
                ((TextInputLayout) view).setError(message);
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String dateAsText = DateUtils.getDateAsString(calendar.getTime());
        deadlineValueTextView.setText(dateAsText);
    }

    public class CurrentPageValidationRule extends QuickRule<TextInputLayout> {

        @Override
        public boolean isValid(TextInputLayout textInputLayout) {
            if (!alreadyStartedCheckBox.isChecked()) return true;

            EditText editText = textInputLayout.getEditText();

            if (editText == null) return true;

            String integerString = editText.getText().toString().trim();

            Integer intValue = getIntegerValue(integerString);
            if (intValue == null) return true;

            Integer totalPages = getIntegerValue(totalPagesEditText.getText().toString().trim());

            return (totalPages == null) || (intValue <= totalPages);
        }

        @Override
        public String getMessage(Context context) {
            return context.getString(R.string.add_book_form_validation_error_current_page_out_of_bounds);
        }
    }
}
