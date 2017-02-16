package com.skedgo.android.tripkit.booking.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.skedgo.android.tripkit.booking.ExternalFormField;
import com.skedgo.android.tripkit.booking.ui.BookingUiModule;
import com.skedgo.android.tripkit.booking.ui.DaggerBookingUiComponent;
import com.skedgo.android.tripkit.booking.ui.R;
import com.skedgo.android.tripkit.booking.ui.databinding.ExternalWebBinding;
import com.skedgo.android.tripkit.booking.ui.viewmodel.ExternalViewModel;

import java.io.Serializable;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import skedgo.rxapp.RxActivity;

import static com.skedgo.android.tripkit.booking.ui.activity.BookingActivity.KEY_URL;

public class ExternalWebActivity extends RxActivity {

  @Inject ExternalViewModel viewModel;

  public static Intent newIntent(
      Context context,
      ExternalFormField externalFormField) {
    return new Intent(context, ExternalWebActivity.class)
        .putExtra("externalFormField", (Serializable) externalFormField);
  }

  @Override public void onDestroy() {
    viewModel.onDestroy();
    super.onDestroy();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final ExternalWebBinding binding = DataBindingUtil.setContentView(this, R.layout.external_web);

    DaggerBookingUiComponent.builder()
        .bookingUiModule(new BookingUiModule(getApplicationContext()))
        .build()
        .inject(this);
    binding.setViewModel(viewModel);

    final Bundle args = getIntent().getExtras();
    viewModel.handleArgs(args);

    binding.webView.getSettings().setJavaScriptEnabled(true);
    binding.webView.setWebViewClient(viewModel.webViewClient());

    viewModel.nextUrlObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .takeUntil(getOnDestroyEvent())
        .subscribe(new Action1<String>() {
          @Override public void call(String nextUrl) {
            final Intent data = new Intent();
            data.putExtra(KEY_URL, nextUrl);
            setResult(Activity.RESULT_OK, data);
            finish();
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable error) {
            Toast.makeText(ExternalWebActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            finish();
          }
        });
  }
}