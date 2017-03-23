package com.skedgo.android.tripkit.booking.viewmodel;

import com.skedgo.android.tripkit.booking.BookingAction;
import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.BookingService;
import com.skedgo.android.tripkit.booking.BuildConfig;
import com.skedgo.android.tripkit.booking.FormField;
import com.skedgo.android.tripkit.booking.FormGroup;
import com.skedgo.android.tripkit.booking.InputForm;
import com.skedgo.android.tripkit.booking.OptionFormField;
import com.skedgo.android.tripkit.booking.StringFormField;
import com.skedgo.android.tripkit.booking.TestRunner;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class BookingViewModelImplTest {
  private AuthenticationViewModel authenticationViewModel;
  private BookingViewModel bookingViewModel;
  private BookingService api;
  private InputForm inputForm;
  private Param param;

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

    bookingViewModel.loadForm(Param.create("url")).toBlocking().single();
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
    bookingViewModel.nextBookingForm().subscribe(new Action1<Param>() {
      @Override
      public void call(Param param) {
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
    api = mock(BookingService.class);
    bookingViewModel = new BookingViewModelImpl(api);
    authenticationViewModel = new AuthenticationViewModelImpl();
    inputForm = InputForm.from(new ArrayList<FormGroup>());
    final BookingAction bookingAction = new BookingAction();
    bookingAction.setUrl("url");
    param = Param.create(bookingAction, inputForm);
  }
}