package com.skedgo.android.tripkit.booking.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.ui.fragment.ExternalProviderAuthFragment;

import static com.skedgo.android.tripkit.booking.ui.activity.BookingActivityKt.KEY_FORM;

public class ExternalProviderAuthActivity extends AppCompatActivity {
  public static Intent newIntent(Context context, BookingForm bookingForm) {
    Intent intent = new Intent(context, ExternalProviderAuthActivity.class);
    intent.putExtra(KEY_FORM, (Parcelable) bookingForm);
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
