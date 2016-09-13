package com.skedgo.android.bookingclient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;

import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.skedgo.android.bookingclient.OAuth2CallbackHandler;
import com.skedgo.android.bookingclient.R;
import com.skedgo.android.bookingclient.fragment.AuthWebFragment;
import com.skedgo.android.bookingclient.fragment.BookingFormFragment;
import com.skedgo.android.bookingclient.fragment.BookingFragment;
import com.skedgo.android.bookingclient.module.BookingClientComponent;
import com.skedgo.android.bookingclient.module.BookingClientModule;
import com.skedgo.android.bookingclient.module.DaggerBookingClientComponent;
import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.FormField;
import com.skedgo.android.tripkit.booking.FormFieldJsonAdapter;
import com.skedgo.android.tripkit.booking.viewmodel.BookingViewModel;
import com.skedgo.android.tripkit.booking.viewmodel.ParamImpl;

import java.io.StringReader;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import skedgo.anim.AnimatedTransitionActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BookingActivity extends AnimatedTransitionActivity implements
    FragmentManager.OnBackStackChangedListener, BookingFormFragment.BookingFormFragmentListener {
  public static final String ACTION_BOOK = "com.skedgo.android.bookingclient.ACTION_BOOK";
  public static final String ACTION_BOOK2 = "com.skedgo.android.bookingclient.ACTION_BOOK2";
  public static final String ACTION_OAUTH = "com.skedgo.android.bookingclient.ACTION_OAUTH";
  public static final String ACTION_BOOK_AFTER_OAUTH = "com.skedgo.android.bookingclient.ACTION_BOOK_AFTER_OAUTH";
  public static final String KEY_URL = "url";
  public static final String KEY_FORM = "param";
  public static final String KEY_FIRST_SCREEN = "firstScreen";
  public static final String KEY_TEMP_BOOKING = "TempBooking";
  public static final String KEY_TEMP_BOOKING_FORM = "TempBookingForm";
  public static final String KEY_BOOKING_BUNDLE = "bookingBundle";
  public static final String KEY_WEB_URL = "web_url";

  public BookingClientComponent component;

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    component = DaggerBookingClientComponent.builder()
        .bookingClientModule(new BookingClientModule(getApplicationContext()))
        .build();

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
    } else if (ACTION_OAUTH.equals(getIntent().getAction()) && getIntent().hasExtra(KEY_WEB_URL)) {

      Uri uri = getIntent().getParcelableExtra(KEY_WEB_URL);
      getSupportFragmentManager()
          .beginTransaction()
          .replace(android.R.id.content, AuthWebFragment.newInstance(uri.toString(), onAuthCallback()))
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

  @Override
  public void onBackStackChanged() {
    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
      getSupportActionBar().setHomeAsUpIndicator(0); // 0 means default
    } else {
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cross);
    }
  }

  public BookingClientComponent getBookingClientComponent() {
    return component;
  }

  public void reportProblem() {

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

  // TODO: Refactor with ExternalProviderAuthFragmentDialog
  @Deprecated
  private Action1<String> onAuthCallback() {

    SharedPreferences prefs = getSharedPreferences(BookingActivity.KEY_TEMP_BOOKING, Activity.MODE_PRIVATE);

    String jsonBooking = prefs.getString(BookingActivity.KEY_TEMP_BOOKING_FORM, "");
    JsonReader reader = new JsonReader(new StringReader(jsonBooking));
    reader.setLenient(true);

    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(FormField.class, new FormFieldJsonAdapter());
    Gson gson = builder.create();

    final BookingForm form = gson.fromJson(reader, BookingForm.class);

    return new Action1<String>() {
      @Override public void call(String url) {
        final BookingClientComponent bookingClientComponent = DaggerBookingClientComponent.builder()
            .bookingClientModule(new BookingClientModule(BookingActivity.this))
            .build();

        OAuth2CallbackHandler oAuth2CallbackHandler = bookingClientComponent.getOAuth2CallbackHandler();
        if (url.startsWith("tripgo://oauth-callback")) {

          String callback = url.substring(0, url.indexOf("?"));

          oAuth2CallbackHandler.handleOAuthURL(form, Uri.parse(url), callback)
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.newThread())
              .subscribe(new Action1<BookingForm>() {
                @Override public void call(BookingForm form) {
                  startActivity(
                      new Intent(BookingActivity.this, BookingActivity.this.getClass())
                          .setAction(BookingActivity.ACTION_BOOK_AFTER_OAUTH)
                          .putExtra(BookingActivity.KEY_FORM, (Parcelable) form));

                  finish();
                }
              }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                  Toast.makeText(BookingActivity.this, R.string.nicely_informed_error, Toast.LENGTH_SHORT).show();
                }
              });
        } else if (url.startsWith("tripgo://booking_retry")) {

          oAuth2CallbackHandler.handleRetryURL(form, Uri.parse(url))
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.newThread())
              .subscribe(new Action1<BookingForm>() {
                @Override public void call(BookingForm form) {
                  startActivity(
                      new Intent(BookingActivity.this, BookingActivity.this.getClass())
                          .setAction(BookingActivity.ACTION_BOOK_AFTER_OAUTH)
                          .putExtra(BookingActivity.KEY_FORM, (Parcelable) form));

                  finish();
                }
              }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                  Toast.makeText(BookingActivity.this, R.string.nicely_informed_error, Toast.LENGTH_SHORT).show();
                }
              });
        }
      }
    };
  }

}

