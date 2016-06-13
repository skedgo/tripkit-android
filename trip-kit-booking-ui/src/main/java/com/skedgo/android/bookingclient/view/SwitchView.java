package com.skedgo.android.bookingclient.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;

import com.skedgo.android.tripkit.booking.SwitchFormField;

public class SwitchView extends AppCompatCheckBox {
  public SwitchView(Context context) {
    super(context);
  }

  public SwitchView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public SwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void bindViewModel(@NonNull final SwitchFormField switchFormField) {
    setVisibility(switchFormField.isHidden() ? View.GONE : View.VISIBLE);
    setText(switchFormField.getTitle());
    setChecked((Boolean) switchFormField.getValue());
    setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switchFormField.setValue(isChecked);
      }
    });
  }
}