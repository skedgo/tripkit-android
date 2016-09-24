package com.skedgo.android.bookingclient.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.skedgo.android.bookingclient.R;
import com.skedgo.android.tripkit.booking.PasswordFormField;

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
    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) { // user finished input
          passwordField.setValue(editText.getText().toString());
        }
      }
    });
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    editText = (EditText) findViewById(R.id.editText);
  }

}