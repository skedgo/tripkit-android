package com.skedgo.android.bookingclient.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skedgo.android.bookingclient.R;
import com.skedgo.android.tripkit.booking.StringFormField;
import com.skedgo.android.bookingclient.view.util.ViewUtils;

public class StringFieldView extends RelativeLayout {
    private TextView titleView;
    private TextView valueView;
    private TextView sideTitleView;
    private EditText editText;

    public StringFieldView(Context context) {
        super(context);
    }

    public StringFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public StringFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StringFieldView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void bindViewModel(@NonNull final StringFormField stringField) {
        setVisibility(stringField.isHidden() ? GONE : VISIBLE);
        titleView.setText(stringField.getTitle());
        ViewUtils.setText(valueView, stringField.getValue());
        ViewUtils.setText(sideTitleView, stringField.getSidetitle());
        if (TextUtils.equals(stringField.getTitle(), stringField.getValue())) {
            valueView.setVisibility(GONE);
        }
        editText.setVisibility(stringField.isReadOnly() ? GONE : VISIBLE);
        if (!stringField.isReadOnly()) {
            if (!stringField.isHidden()) {
                stringField.setValue(editText.getText().toString());
            }
            titleView.setVisibility(GONE);
            valueView.setVisibility(GONE);
            editText.setHint(stringField.getTitle());
            editText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) { // user finished input
                        stringField.setValue(editText.getText().toString());
                    }
                }
            });
            if (stringField.getKeyboardType() != null) {
                switch (stringField.getKeyboardType()) {
                    case "PHONE":
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER
                                | InputType.TYPE_NUMBER_FLAG_DECIMAL
                                | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        break;
                    case "EMAIL":
                        editText.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        break;
                }
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleView = (TextView) findViewById(R.id.titleView);
        valueView = (TextView) findViewById(R.id.valueView);
        sideTitleView = (TextView) findViewById(R.id.sideTitleView);
        editText = (EditText) findViewById(R.id.editText);
    }
}