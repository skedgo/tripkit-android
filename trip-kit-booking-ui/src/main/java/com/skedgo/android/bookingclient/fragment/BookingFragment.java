package com.skedgo.android.bookingclient.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.skedgo.android.bookingclient.R;
import com.skedgo.android.bookingclient.activity.BookingActivity;
import com.skedgo.android.bookingclient.viewmodel.BookingErrorViewModel;
import com.skedgo.android.bookingclient.viewmodel.ExtendedBookingViewModel;
import com.skedgo.android.common.util.LogUtils;
import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.LinkFormField;
import com.skedgo.android.tripkit.booking.viewmodel.BookingViewModel;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import dagger.Lazy;
import rx.android.view.ViewActions;
import rx.functions.Action1;
import skedgo.common.view.ButterKnifeFragment;

public class BookingFragment extends ButterKnifeFragment implements View.OnClickListener {
  public static final String KEY_PARAM = "param";
  public static final String KEY_FORM = "form";
  private static final String TAG_BOOKING_FORM = "bookingForm";
  private static final String EXTRA_DONE = "done";

  @Inject ExtendedBookingViewModel viewModel;
  @Inject Bus bus;
  ProgressBar progressView;
  TextView hudTextView;
  @Inject Lazy<BookingErrorViewModel> bookingErrorViewModel;
  @Inject Gson gson;
  TextView backButton;
  TextView errorTitleView;
  TextView errorMessageView;

  public static BookingFragment newInstance(BookingViewModel.Param param) {
    final Bundle args = new Bundle();
    args.putParcelable(KEY_PARAM, param);

    final BookingFragment fragment = new BookingFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static BookingFragment newInstance(BookingForm form) {
    final Bundle args = new Bundle();
    args.putParcelable(KEY_FORM, form);

    final BookingFragment fragment = new BookingFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    setContentLayout(R.layout.fragment_booking);
    if (getActivity() instanceof BookingActivity) {
      ((BookingActivity) getActivity()).getBookingClientComponent().inject(this);
    }

  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_booking, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.reportProblemMenuItem) {

      if (getActivity() instanceof BookingActivity) {
        BookingActivity a = (BookingActivity) getActivity();
        a.reportProblem();
      }
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    bus.register(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    bus.unregister(this);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    BookingViewModel.Param param = null;

    if (getArguments().containsKey(KEY_PARAM)) {
      param = getArguments().getParcelable(KEY_PARAM);
    } else if (getArguments().containsKey(KEY_FORM)) {
      BookingForm form = getArguments().getParcelable(KEY_FORM);

      param = viewModel.paramFrom(form);
    }

    viewModel.loadForm(param)
        .takeUntil(lifecycle().onDestroy())
        .subscribe(new Action1<BookingForm>() {
          @Override public void call(BookingForm form) {
            // Server can return a null form indicating end of the process, in authentication for example
            if (form == null) {
              getActivity().finish();
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable error) {
            LogUtils.LOGE(TAG_BOOKING_FORM, "Error on booking", error);
            showError(error);
          }
        });

    if (param != null) {
      hudTextView.setText(param.getHudText());
    }

  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    progressView = (ProgressBar) view.findViewById(R.id.progressView);
    hudTextView = (TextView) view.findViewById(R.id.hudTextView);

    viewModel.isFetching()
        .takeUntil(lifecycle().onDestroyView())
        .subscribe(ViewActions.setVisibility(progressView));

    viewModel.isFetching()
        .takeUntil(lifecycle().onDestroyView())
        .subscribe(ViewActions.setVisibility(hudTextView));

    viewModel.isFetching()
        .takeUntil(lifecycle().onDestroyView())
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean value) {
            final Fragment fragment = getFragmentManager().findFragmentByTag(TAG_BOOKING_FORM);
            if (fragment != null) {
              if (value) {
                getFragmentManager()
                    .beginTransaction()
                    .hide(fragment)
                    .commit();
              } else {
                getFragmentManager()
                    .beginTransaction()
                    .show(fragment)
                    .commit();
              }
            }
          }
        });

    viewModel.isDone()
        .takeUntil(lifecycle().onDestroyView())
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean success) {
            Intent data = new Intent();
            data.putExtra(EXTRA_DONE, true);
            if (success) {
              getActivity().setResult(Activity.RESULT_OK, data);
            } else {
              getActivity().setResult(Activity.RESULT_CANCELED, data);
            }
            getActivity().finish();
          }
        });

    viewModel.nextBookingForm()
        .takeUntil(lifecycle().onDestroyView())
        .subscribe(new Action1<BookingViewModel.Param>() {
          @Override
          public void call(BookingViewModel.Param param) {
            showNewBookingForm(param);
          }
        });

    viewModel.bookingForm()
        .takeUntil(lifecycle().onDestroyView())
        .subscribe(new Action1<BookingForm>() {
          @Override
          public void call(BookingForm form) {
            if (form.isOAuthForm()) {

              // Check oauth info

              showAuthentication(form);
            } else {
              showBookingForm(form);
            }
          }
        });
  }

  @Subscribe
  public void onEvent(BookingFormFragment.LinkFormFieldClickedEvent event) {
    final LinkFormField linkField = event.linkField;
    final String method = linkField.getMethod();
    viewModel.performAction(linkField).subscribe();
  }

  @Subscribe
  public void onEvent(BookingFormFragment.PerformActionEvent event) {
    viewModel.performAction(event.form).subscribe();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      final Bundle bundle = getActivity().getIntent().getBundleExtra(BookingActivity.KEY_BOOKING_BUNDLE);
      if (bundle != null) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
      } else {
        getActivity().setResult(Activity.RESULT_OK);
      }
      getActivity().finish();
    } else if (data != null && data.getBooleanExtra(EXTRA_DONE, false)) {
      Intent done = new Intent();
      done.putExtra(EXTRA_DONE, true);
      getActivity().setResult(resultCode, done);
      getActivity().finish();
    }
  }

  @Override
  public void onClick(View v) {
    getActivity().onBackPressed();
  }

  private void showError(Throwable error) {
    checkAndInflate();

    bookingErrorViewModel.get().read(error.getMessage());
    errorTitleView.setText(bookingErrorViewModel.get().getErrorTitle());
    errorMessageView.setText(bookingErrorViewModel.get().getErrorMessage());
  }

  private void checkAndInflate() {
    View rootView = getView();
    if (rootView == null) {
      return;
    }

    ViewStub viewStub = (ViewStub) rootView.findViewById(R.id.errorLayout);
    if (viewStub != null) { // not inflated yet
      View errorLayout = viewStub.inflate();
      backButton = (TextView) errorLayout.findViewById(R.id.backButton);
      errorTitleView = (TextView) errorLayout.findViewById(R.id.errorTitleView);
      errorMessageView = (TextView) errorLayout.findViewById(R.id.errorMessageView);
      backButton.setOnClickListener(this);
      progressView.setVisibility(View.GONE);
      hudTextView.setVisibility(View.GONE);
    }
  }

  private void showAuthentication(@NonNull final BookingForm form) {

    viewModel.needsAuthentication(form).subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean needAuth) {
        if (needAuth) {
          // save temp booking
          SharedPreferences prefs = getActivity().getSharedPreferences(BookingActivity.KEY_TEMP_BOOKING, Activity.MODE_PRIVATE);
          SharedPreferences.Editor prefsEditor = prefs.edit();

          String json = gson.toJson(form);
          prefsEditor.putString(BookingActivity.KEY_TEMP_BOOKING_FORM, json);
          prefsEditor.apply();

          Intent intent = new Intent(Intent.ACTION_VIEW, form.getOAuthLink());
          startActivity(intent);

        } else {
          startActivity(
              new Intent(getActivity(), getActivity().getClass())
                  .setAction(BookingActivity.ACTION_BOOK_AFTER_OAUTH)
                  .putExtra(BookingActivity.KEY_FORM, (Parcelable) form));
        }
        getActivity().finish();
      }
    });

  }

  private void showBookingForm(BookingForm form) {
    final Fragment oldFragment = getFragmentManager().findFragmentByTag(TAG_BOOKING_FORM);
    if (oldFragment != null) {
      getFragmentManager()
          .beginTransaction()
          .remove(oldFragment)
          .commit();
    }

    getFragmentManager()
        .beginTransaction()
        .add(android.R.id.content, BookingFormFragment.newInstance(form), TAG_BOOKING_FORM)
        .commit();
  }

  private void showNewBookingForm(BookingViewModel.Param param) {
    final Intent intent = new Intent(BookingActivity.ACTION_BOOK2);
    intent.putExtra("param", param);
    startActivityForResult(intent, 0);
  }
}