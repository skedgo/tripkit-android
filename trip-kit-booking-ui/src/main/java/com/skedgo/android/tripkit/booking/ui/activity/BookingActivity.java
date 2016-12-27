package com.skedgo.android.tripkit.booking.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.ui.fragment.BookingFormFragment;
import com.skedgo.android.tripkit.booking.ui.fragment.BookingFragment;
import com.skedgo.android.tripkit.booking.ui.module.BookingClientComponent;
import com.skedgo.android.tripkit.booking.ui.module.BookingClientModule;
import com.skedgo.android.tripkit.booking.ui.module.DaggerBookingClientComponent;
import com.skedgo.android.tripkit.booking.viewmodel.Param;
import com.skedgo.android.tripkit.booking.viewmodel.ParamImpl;

import skedgo.anim.AnimatedTransitionActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BookingActivity extends AnimatedTransitionActivity implements
    BookingFormFragment.BookingFormFragmentListener {
  public static final String ACTION_BOOK = "com.skedgo.android.tripkit.booking.ui.ACTION_BOOK";
  public static final String ACTION_BOOK2 = "com.skedgo.android.tripkit.booking.ui.ACTION_BOOK2";
  public static final String KEY_URL = "url";
  public static final String KEY_FIRST_SCREEN = "firstScreen";
  public static final String KEY_BOOKING_BUNDLE = "bookingBundle";
  public static final String KEY_BOOKING_FORM = "bookingForm";

  public BookingClientComponent component;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    component = DaggerBookingClientComponent.builder()
        .bookingClientModule(new BookingClientModule(getApplicationContext()))
        .build();

    setupActionBar();

    final String action = getIntent().getAction();
    if (ACTION_BOOK.equals(action)) {
      final String url = getIntent().getStringExtra(KEY_URL);
      getSupportFragmentManager()
          .beginTransaction()
          .add(android.R.id.content, BookingFragment.newInstance(ParamImpl.create(url)))
          .commit();
    } else if (ACTION_BOOK2.equals(action)) {
      final Param param = getIntent().getParcelableExtra("param");
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

  public BookingClientComponent getBookingClientComponent() {
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
