package com.skedgo.android.tripkit.booking.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.ui.BookingUiComponent;
import com.skedgo.android.tripkit.booking.ui.BookingUiModule;
import com.skedgo.android.tripkit.booking.ui.DaggerBookingUiComponent;
import com.skedgo.android.tripkit.booking.ui.fragment.BookingFormFragment;
import com.skedgo.android.tripkit.booking.ui.fragment.BookingFragment;
import com.skedgo.android.tripkit.booking.viewmodel.Param;

import skedgo.anim.AnimatedTransitionActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BookingActivity extends AnimatedTransitionActivity implements
    BookingFormFragment.BookingFormFragmentListener {
  public static final String ACTION_BOOK = "com.skedgo.android.tripkit.booking.ui.ACTION_BOOK";
  public static final String KEY_URL = "url";
  public static final String KEY_FIRST_SCREEN = "firstScreen";
  public static final String KEY_BOOKING_BUNDLE = "bookingBundle";
  public static final String KEY_BOOKING_FORM = "bookingForm";
  public static final String ACTION_BOOK2 = "com.skedgo.android.tripkit.booking.ui.ACTION_BOOK2";
  private BookingUiComponent component;

  public static Intent newIntent(Context context, Param param) {
    final Intent intent = new Intent(BookingActivity.ACTION_BOOK2);

    // Limits the components that will resolve this Intent
    // to those within the app utilizing this library.
    // Otherwise, the app will run into this issue
    // https://github.com/skedgo/tripgo-android/issues/1689
    // that would crash android system process.
    intent.setPackage(context.getPackageName());
    intent.putExtra("param", param);
    return intent;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    component = DaggerBookingUiComponent.builder()
        .bookingUiModule(new BookingUiModule(getApplicationContext()))
        .build();

    setupActionBar();

    final Intent intent = getIntent();
    final String action = intent.getAction();
    if (ACTION_BOOK.equals(action)) {
      final String url = intent.getStringExtra(KEY_URL);
      getSupportFragmentManager()
          .beginTransaction()
          .add(android.R.id.content, BookingFragment.newInstance(Param.create(url)))
          .commit();
    } else if (ACTION_BOOK2.equals(action)) {
      final Param param = intent.getParcelableExtra("param");
      getSupportFragmentManager()
          .beginTransaction()
          .add(android.R.id.content, BookingFragment.newInstance(param))
          .commit();
    } else {
      throw new IllegalStateException("Unknown action: " + action);
    }
  }

  @Override
  public void updateTitleEvent(BookingForm form) {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(form.getTitle());
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  public BookingUiComponent getBookingClientComponent() {
    return component;
  }

  public void reportProblem() {}

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  private void setupActionBar() {
    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayShowHomeEnabled(true);
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }
}
