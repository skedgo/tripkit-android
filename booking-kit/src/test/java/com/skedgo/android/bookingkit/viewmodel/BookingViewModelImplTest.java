package com.skedgo.android.bookingkit.viewmodel;

import com.skedgo.android.bookingkit.BuildConfig;
import com.skedgo.android.bookingkit.api.BookingApi;
import com.skedgo.android.bookingkit.model.BookingAction;
import com.skedgo.android.bookingkit.model.BookingForm;
import com.skedgo.android.bookingkit.model.FormField;
import com.skedgo.android.bookingkit.model.FormGroup;
import com.skedgo.android.bookingkit.model.InputForm;
import com.skedgo.android.bookingkit.model.OptionFormField;
import com.skedgo.android.bookingkit.model.StringFormField;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class BookingViewModelImplTest {
  private AuthenticationViewModel authenticationViewModel;
  private BookingViewModel bookingViewModel;
  private BookingApi api;
  private InputForm inputForm;
  private BookingViewModel.Param param;

  @Test public void fetchNextBookingFormIfAuthenticationSucceeds() {
    when(api.getFormAsync(param.getUrl()))
        .thenReturn(Observable.just(new BookingForm()));
    when(api.postFormAsync(param.getUrl(), param.postBody()))
        .thenReturn(Observable.just(new BookingForm()));

    bookingViewModel.loadForm(param).toBlocking().single(); // call postFormAsync one time here
    bookingViewModel.observeAuthentication(authenticationViewModel);
    authenticationViewModel.verify("www.skedgo.com").toBlocking().single();
    verify(api, times(2)).postFormAsync("url", inputForm); // should call postFormAsync one more time
  }

  @Test public void doNothingIfAuthenticationFails() {
    when(api.getFormAsync(param.getUrl()))
        .thenReturn(Observable.just(new BookingForm()));
    when(api.postFormAsync(param.getUrl(), param.postBody()))
        .thenReturn(Observable.just(new BookingForm()));

    bookingViewModel.loadForm(param).toBlocking().single(); // call postFormAsync one time here
    bookingViewModel.observeAuthentication(authenticationViewModel);
    authenticationViewModel.verify("www.google.com").toBlocking().single();
    verify(api, times(1)).postFormAsync("url", inputForm); // should call no more postFormAsync
    verify(api, never()).getFormAsync("url"); // should never call getFormAsync
  }

  @Test public void reloadBookingForm() {
    final BookingForm bookingForm = new BookingForm();
    bookingForm.setId("1");
    when(api.postFormAsync(param.getUrl(), param.postBody()))
        .thenReturn(Observable.just(bookingForm));

    bookingViewModel.loadForm(param).toBlocking().single();
    BookingForm actual = bookingViewModel.bookingForm().toBlocking().first();
    assertThat(actual.getId()).isEqualTo("1");
  }

  @Test public void getBooking() {
    final BookingForm bookingForm = new BookingForm();
    bookingForm.setId("2");
    when(api.getFormAsync("url"))
        .thenReturn(Observable.just(bookingForm));

    bookingViewModel.loadForm(ParamImpl.create("url")).toBlocking().single();
    BookingForm actual = bookingViewModel.bookingForm().toBlocking().first();
    assertThat(actual.getId()).isEqualTo("2");
  }

  @Test public void nextBookingForm() {
    BookingForm bookingFormItem = new BookingForm();
    List<FormGroup> formGroupList = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      List<FormField> bookingItemList = new ArrayList<>();
      StringFormField stringFormField = new StringFormField();
      bookingItemList.add(stringFormField);
      OptionFormField optionFormField = new OptionFormField();
      bookingItemList.add(optionFormField);

      FormGroup item = new FormGroup();
      item.setFields(bookingItemList);
      formGroupList.add(item);
    }

    bookingFormItem.setForm(formGroupList);
    BookingAction bookingAction = new BookingAction();
    bookingAction.setUrl("url2");
    bookingFormItem.setAction(bookingAction);
    bookingViewModel.performAction(bookingFormItem).toBlocking().single();
    bookingViewModel.nextBookingForm().subscribe(new Action1<BookingViewModel.Param>() {
      @Override
      public void call(BookingViewModel.Param param) {
        assertThat(param.getUrl()).isEqualTo("url2");
        assertThat(param.postBody().input()).isNotNull();
        assertThat(param.postBody().input()).hasSize(4);
        List<FormField> list = param.postBody().input();
        assertThat(list.get(0)).isInstanceOf(StringFormField.class);
        assertThat(list.get(1)).isInstanceOf(OptionFormField.class);
        assertThat(list.get(2)).isInstanceOf(StringFormField.class);
        assertThat(list.get(3)).isInstanceOf(OptionFormField.class);
      }
    });
  }

  @Test public void threeTimesOnFailureOfFetchingBookingForm() {
    when(api.getFormAsync(anyString()))
        .thenReturn(Observable.create(new Observable.OnSubscribe<BookingForm>() {
          final AtomicInteger retryCounter = new AtomicInteger(0);

          @Override
          public void call(Subscriber<? super BookingForm> subscriber) {
            if (retryCounter.incrementAndGet() == 3) {
              // Ok, this time, let it succeed!
              subscriber.onNext(new BookingForm());
              subscriber.onCompleted();
            } else {
              subscriber.onError(new Exception("Failed to retrieve booking form"));
            }
          }
        }));

    try {
      bookingViewModel.loadForm(ParamImpl.create("http://facebook.github.io/stetho/"))
          .toBlocking().single();
    } catch (Exception e) {
      Assertions.fail("Retry fetching booking form 3 times on any failure", e);
    }
  }

  @Test public void doNotCrashWithNullableParam() {
    final Observable<BookingForm> observable;
    try {
      observable = bookingViewModel.loadForm(null);
    } catch (NullPointerException e) {
      Assertions.fail("Deal with nullable command param", e);
      return;
    }

    try {
      observable.toBlocking().single();
      Assertions.fail("Propagate error passing invalid param");
    } catch (NullPointerException e) {
    }
  }

  @Test public void handleCanceledBooking() {
    final BookingForm bookingForm = new BookingForm();
    final FormGroup formGroup = new FormGroup();
    final ArrayList<FormField> fields = new ArrayList<>();
    final StringFormField stringFormField = new StringFormField();
    stringFormField.setValue("Cancelled");
    fields.add(stringFormField);
    formGroup.setFields(fields);
    bookingForm.setAction(new BookingAction());
    bookingForm.setForm(Arrays.asList(formGroup));
    when(api.getFormAsync(param.getUrl()))
        .thenReturn(Observable.just(bookingForm));
    BookingViewModelImpl bookingViewModel = new BookingViewModelImpl(api);

    bookingViewModel.performAction(bookingForm);
    final TestSubscriber<Boolean> subscriber = new TestSubscriber<>();
    bookingViewModel.isDone().subscribe(subscriber);
    subscriber.assertNoErrors();
    subscriber.assertReceivedOnNext(Arrays.asList(false));
  }

  @Test public void handleSucceedBooking() {
    final BookingForm bookingForm = new BookingForm();
    final FormGroup formGroup = new FormGroup();
    final ArrayList<FormField> fields = new ArrayList<>();
    final StringFormField stringFormField = new StringFormField();
    stringFormField.setValue("Confirmed");
    fields.add(stringFormField);
    formGroup.setFields(fields);
    bookingForm.setAction(new BookingAction());
    bookingForm.setForm(Arrays.asList(formGroup));
    when(api.getFormAsync(param.getUrl()))
        .thenReturn(Observable.just(bookingForm));
    BookingViewModelImpl bookingViewModel = new BookingViewModelImpl(api);

    bookingViewModel.performAction(bookingForm);
    final TestSubscriber<Boolean> subscriber = new TestSubscriber<>();
    bookingViewModel.isDone().subscribe(subscriber);
    subscriber.assertNoErrors();
    subscriber.assertReceivedOnNext(Arrays.asList(true));
  }

  @Before public void before() {
    api = mock(BookingApi.class);
    bookingViewModel = new BookingViewModelImpl(api);
    authenticationViewModel = new AuthenticationViewModelImpl();
    inputForm = InputForm.from(new ArrayList<FormGroup>());
    final BookingAction bookingAction = new BookingAction();
    bookingAction.setUrl("url");
    param = ParamImpl.create(bookingAction, inputForm);
  }
}