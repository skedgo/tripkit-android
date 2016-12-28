package com.skedgo.android.tripkit.booking.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.ui.fragment.ExternalProviderAuthFragment;

import skedgo.anim.AnimatedTransitionActivity;

import static com.skedgo.android.tripkit.booking.ui.activity.BookingActivity.KEY_BOOKING_FORM;

public class ExternalProviderAuthActivity extends AnimatedTransitionActivity {
  public static Intent newIntent(Context context, BookingForm bookingForm) {
    Intent intent = new Intent(context, ExternalProviderAuthActivity.class);
    intent.putExtra(KEY_BOOKING_FORM, (Parcelable) bookingForm);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      final Bundle extras = getIntent().getExtras();
      getSupportFragmentManager()
          .beginTransaction()
          .replace(android.R.id.content, ExternalProviderAuthFragment.newInstance(extras))
          .commit();
    }
  }
}
