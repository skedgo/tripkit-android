package com.skedgo.android.bookingclient.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.skedgo.android.bookingclient.R;
import com.skedgo.android.bookingclient.fragment.BookingFormFragment;
import com.skedgo.android.bookingclient.fragment.BookingFragment;
import com.skedgo.android.bookingclient.module.BookingClientComponent;
import com.skedgo.android.bookingclient.module.BookingClientModule;
import com.skedgo.android.bookingclient.module.DaggerBookingClientComponent;
import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.viewmodel.BookingViewModel;
import com.skedgo.android.tripkit.booking.viewmodel.ParamImpl;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import skedgo.anim.AnimatedTransitionActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BookingActivity extends AnimatedTransitionActivity implements FragmentManager.OnBackStackChangedListener {
  public static final String ACTION_BOOK = "com.skedgo.android.bookingclient.ACTION_BOOK";
  public static final String ACTION_BOOK2 = "com.skedgo.android.bookingclient.ACTION_BOOK2";
  public static final String ACTION_BOOK_AFTER_OAUTH = "com.skedgo.android.bookingclient.ACTION_BOOK_AFTER_OAUTH";
  public static final String KEY_URL = "url";
  public static final String KEY_FORM = "param";
  public static final String KEY_FIRST_SCREEN = "firstScreen";
  public static final String KEY_TEMP_BOOKING = "TempBooking";
  public static final String KEY_TEMP_BOOKING_FORM = "TempBookingForm";
  public static final String KEY_BOOKING_BUNDLE = "bookingBundle";

  public static BookingClientComponent component;

  @Inject Bus bus;

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override public void onStart() {
    super.onStart();
    bus.register(this);
  }

  @Override protected void onStop() {
    super.onStop();
    bus.unregister(this);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    component = DaggerBookingClientComponent.builder()
        .bookingClientModule(new BookingClientModule(getApplicationContext()))
        .build();

    component.inject(this);

    setupActionBar();

    if (ACTION_BOOK.equals(getIntent().getAction())) {
      final String url = getIntent().getStringExtra(KEY_URL);
      getSupportFragmentManager()
          .beginTransaction()
          .add(android.R.id.content, BookingFragment.newInstance(ParamImpl.create(url)))
          .commit();
    } else if (ACTION_BOOK2.equals(getIntent().getAction())) {
      final BookingViewModel.Param param = getIntent().getParcelableExtra("param");
      getSupportFragmentManager()
          .beginTransaction()
          .add(android.R.id.content, BookingFragment.newInstance(param))
          .commit();
    } else if (ACTION_BOOK_AFTER_OAUTH.equals(getIntent().getAction()) && getIntent().hasExtra(KEY_FORM)) {

      BookingForm form = getIntent().getParcelableExtra(KEY_FORM);

      getSupportFragmentManager()
          .beginTransaction()
          .add(android.R.id.content, BookingFragment.newInstance(form))
          .commit();
    } else {
      Toast.makeText(this, "Undefined action", Toast.LENGTH_SHORT).show();
      finish();
    }
  }

  @Subscribe
  public void updateTitleEvent(BookingFormFragment.UpdateTitleEvent event) {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(event.form.getTitle());
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

  @Override
  public void onBackStackChanged() {
    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
      getSupportActionBar().setHomeAsUpIndicator(0); // 0 means default
    } else {
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cross);
    }
  }

  private void setupActionBar() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayShowHomeEnabled(true);
      actionBar.setDisplayHomeAsUpEnabled(true);
      if (getIntent().getBooleanExtra(KEY_FIRST_SCREEN, false)) {
        actionBar.setHomeAsUpIndicator(R.drawable.ic_cross);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
      }
    }
  }
}