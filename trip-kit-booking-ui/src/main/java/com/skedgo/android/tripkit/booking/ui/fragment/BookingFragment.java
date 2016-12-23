package com.skedgo.android.tripkit.booking.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skedgo.android.common.util.LogUtils;
import com.skedgo.android.tripkit.booking.BookingError;
import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.LinkFormField;
import com.skedgo.android.tripkit.booking.ui.R;
import com.skedgo.android.tripkit.booking.ui.activity.BookingActivity;
import com.skedgo.android.tripkit.booking.ui.activity.ExternalProviderAuthActivity;
import com.skedgo.android.tripkit.booking.ui.viewmodel.ExtendedBookingViewModel;
import com.skedgo.android.tripkit.booking.viewmodel.BookingViewModel;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import rx.functions.Action1;
import skedgo.common.view.ButterKnifeFragment;

import static com.skedgo.android.tripkit.booking.ui.activity.BookingActivity.KEY_BOOKING_FORM;

public class BookingFragment extends ButterKnifeFragment implements View.OnClickListener {
  public static final String KEY_PARAM = "param";
  public static final String KEY_FORM = "form";
  private static final String TAG_BOOKING_FORM = "bookingForm";
  private static final String EXTRA_DONE = "done";

  private static final int RQ_EXTERNAL = 1;

  @Nullable private BookingForm bookingForm = null;

  @Inject ExtendedBookingViewModel viewModel;
  @Inject Bus bus;
  ProgressBar progressView;
  TextView hudTextView;
  TextView backButton;
  TextView errorTitleView;
  TextView errorMessageView;

  private Action1<Throwable> errorAction = new Action1<Throwable>() {
    @Override
    public void call(Throwable error) {
      LogUtils.LOGE(TAG_BOOKING_FORM, "Error on booking", error);
      showError(error);
    }
  };

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

  static Action1<? super Boolean> setVisibility(final View view) {
    return new Action1<Boolean>() {
      @Override public void call(Boolean v) {
        view.setVisibility(v ? View.VISIBLE : View.GONE);
      }
    };
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
        }, errorAction);

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
        .subscribe(setVisibility(progressView));

    viewModel.isFetching()
        .takeUntil(lifecycle().onDestroyView())
        .subscribe(setVisibility(hudTextView), errorAction);

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
        }, errorAction);

    viewModel.isDone()
        .takeUntil(lifecycle().onDestroyView())
        .subscribe(new Action1<Boolean>() {
          @Override
          public void call(Boolean success) {
            Intent data = new Intent();
            data.putExtra(EXTRA_DONE, true);

            if (bookingForm != null) {
              data.putExtra(KEY_BOOKING_FORM, (Parcelable) bookingForm);
            }

            if (success) {
              getActivity().setResult(Activity.RESULT_OK, data);
            } else {
              getActivity().setResult(Activity.RESULT_CANCELED, data);
            }
            getActivity().finish();
          }
        }, errorAction);

    viewModel.nextBookingForm()
        .takeUntil(lifecycle().onDestroyView())
        .subscribe(new Action1<BookingViewModel.Param>() {
          @Override
          public void call(BookingViewModel.Param param) {
            showNewBookingForm(param);
          }
        }, errorAction);

    viewModel.bookingForm()
        .takeUntil(lifecycle().onDestroyView())
        .subscribe(new Action1<BookingForm>() {
          @Override
          public void call(BookingForm form) {
            showBookingForm(form);
          }
        }, errorAction);
  }

  @Subscribe
  public void onEvent(BookingFormFragment.LinkFormFieldClickedEvent event) {
    final LinkFormField linkField = event.linkField;
    final String method = linkField.getMethod();

    if (LinkFormField.METHOD_EXTERNAL.equals(linkField.getMethod())) {
      Intent intent = ExternalProviderAuthActivity.newIntent(getActivity(), bookingForm);
      startActivityForResult(intent, RQ_EXTERNAL);
    } else {
      viewModel.performAction(linkField).subscribe();
    }

  }

  @Subscribe
  public void onEvent(BookingFormFragment.PerformActionEvent event) {
    viewModel.performAction(event.form).subscribe();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == RQ_EXTERNAL) {
        Intent done = new Intent();
        done.putExtra(EXTRA_DONE, true);
        getActivity().setResult(resultCode, done);
        getActivity().finish();
      } else {
        final Bundle bundle = getActivity().getIntent().getBundleExtra(BookingActivity.KEY_BOOKING_BUNDLE);
        if (bundle != null) {
          Intent intent = new Intent();
          intent.putExtras(bundle);
          getActivity().setResult(Activity.RESULT_OK, intent);
        } else {
          getActivity().setResult(Activity.RESULT_OK, data);
        }
        getActivity().finish();
      }

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
    if (error instanceof BookingError) {
      BookingError bookingError = (BookingError) error;
      checkAndInflate();
      if (bookingError.getTitle() != null) {
        errorTitleView.setText(bookingError.getTitle());
      }
      if (bookingError.getError() != null) {
        errorMessageView.setText(bookingError.getError());
      }
    }
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

  private void showBookingForm(BookingForm form) {

    this.bookingForm = form;

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