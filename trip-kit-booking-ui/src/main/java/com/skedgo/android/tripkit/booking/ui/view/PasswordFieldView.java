package com.skedgo.android.tripkit.booking.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.skedgo.android.tripkit.booking.PasswordFormField;
import com.skedgo.android.tripkit.booking.ui.R;

public class PasswordFieldView extends RelativeLayout {
  private EditText editText;

  public PasswordFieldView(Context context) {
    super(context);
  }

  public PasswordFieldView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public PasswordFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public PasswordFieldView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  public void bindViewModel(@NonNull final PasswordFormField passwordField) {
    setVisibility(passwordField.isHidden() ? GONE : VISIBLE);
    editText.setHint(passwordField.getTitle());
    editText.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

          @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

          public void afterTextChanged(Editable s) {
            passwordField.setValue(s.toString());
          }
        }
    );

  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    editText = (EditText) findViewById(R.id.editText);
  }

}