package com.skedgo.android.tripkit.booking.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skedgo.android.tripkit.booking.ui.R;
import com.skedgo.android.tripkit.booking.ui.view.util.ViewUtils;

public class TwoLineFieldView extends RelativeLayout {
  private TextView primaryTextView;
  private TextView secondaryTextView;
  private ImageView imageView;
  private View dividerView;

  public TwoLineFieldView(Context context) {
    super(context);
  }

  public TwoLineFieldView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public TwoLineFieldView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public TwoLineFieldView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  public void setPrimaryText(@Nullable CharSequence primaryText) {
    primaryTextView.setText(primaryText);
  }

  public void setSecondaryText(@Nullable CharSequence secondaryText) {
    ViewUtils.setText(secondaryTextView, secondaryText);
  }

  public ImageView getImageView() {
    imageView.setVisibility(VISIBLE);
    return (imageView);
  }

  public void enableDivider() {
    dividerView.setVisibility(VISIBLE);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    primaryTextView = (TextView) findViewById(R.id.titleView);
    secondaryTextView = (TextView) findViewById(R.id.valueView);
    imageView = (ImageView) findViewById(R.id.imageView);
    dividerView = findViewById(R.id.dividerView);
  }
}