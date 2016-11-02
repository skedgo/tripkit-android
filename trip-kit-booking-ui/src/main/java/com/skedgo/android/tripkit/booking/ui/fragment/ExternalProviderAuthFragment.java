package com.skedgo.android.tripkit.booking.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.skedgo.android.common.util.LogUtils;
import com.skedgo.android.tripkit.booking.ui.OAuth2CallbackHandler;
import com.skedgo.android.tripkit.booking.ui.R;
import com.skedgo.android.tripkit.booking.ui.databinding.ExternalProviderAuthBinding;
import com.skedgo.android.tripkit.booking.ui.module.BookingClientModule;
import com.skedgo.android.tripkit.booking.ui.module.DaggerBookingClientComponent;
import com.skedgo.android.tripkit.booking.ui.viewmodel.ExternalProviderAuthViewModel;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import skedgo.common.view.ButterKnifeFragment;

public class ExternalProviderAuthFragment extends ButterKnifeFragment {
  private static final String TAG_EXTERNAL_AUTH = "externalAuth";
  @Inject ExternalProviderAuthViewModel viewModel;
  @Inject OAuth2CallbackHandler oAuth2CallbackHandler;

  public static ExternalProviderAuthFragment newInstance(Bundle args) {
    ExternalProviderAuthFragment fragment = new ExternalProviderAuthFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentLayout(R.layout.external_provider_auth);
    DaggerBookingClientComponent.builder()
        .bookingClientModule(new BookingClientModule(getContext().getApplicationContext()))
        .build()
        .inject(this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    viewModel.onDestroy();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    ExternalProviderAuthBinding binding = ExternalProviderAuthBinding.bind(view);
    binding.setViewModel(viewModel);

    // hack to solve web view keyboard issue
    binding.edit.setFocusable(true);
    binding.edit.requestFocus();

    viewModel.handleArgs(getArguments());

    binding.webView.getSettings().setJavaScriptEnabled(true);
    binding.webView.getSettings().setLoadWithOverviewMode(true);
    binding.webView.getSettings().setUseWideViewPort(true);
    binding.webView.setWebViewClient(viewModel.webViewClient(oAuth2CallbackHandler));

    viewModel.intentObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .takeUntil(lifecycle().onDestroy())
        .subscribe(new Action1<Intent>() {
          @Override public void call(Intent intent) {
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().finish();
            LogUtils.LOGE(TAG_EXTERNAL_AUTH, "Error on external provider auth", throwable);
          }
        });
  }
}