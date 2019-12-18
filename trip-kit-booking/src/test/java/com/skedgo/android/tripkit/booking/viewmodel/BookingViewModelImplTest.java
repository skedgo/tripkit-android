package com.skedgo.android.tripkit.booking.viewmodel;

import com.skedgo.android.tripkit.booking.BookingAction;
import com.skedgo.android.tripkit.booking.BookingForm;
import com.skedgo.android.tripkit.booking.BookingService;
import com.skedgo.android.tripkit.booking.FormField;
import com.skedgo.android.tripkit.booking.FormGroup;
import com.skedgo.android.tripkit.booking.InputForm;
import com.skedgo.android.tripkit.booking.OptionFormField;
import com.skedgo.android.tripkit.booking.StringFormField;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.subscribers.TestSubscriber;
import org.assertj.core.api.Assertions;
import org.intellij.lang.annotations.Flow;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class BookingViewModelImplTest {
  private AuthenticationViewModel authenticationViewModel;
  private BookingViewModel bookingViewModel;
  private BookingService api;
  private InputForm inputForm;
  private Param param;

  @Test public void fetchNextBookingFormIfAuthenticationSucceeds() {
    when(api.getFormAsync(param.getUrl()))
        .thenReturn(Flowable.just(new BookingForm()));
    when(api.postFormAsync(param.getUrl(), param.postBody()))
        .thenReturn(Flowable.just(new BookingForm()));

    bookingViewModel.loadForm(param).blockingSingle(); // call postFormAsync one time here
    bookingViewModel.observeAuthentication(authenticationViewModel);
    authenticationViewModel.verify("www.skedgo.com").blockingSingle();
    verify(api, times(1)).postFormAsync("url", inputForm); // should call postFormAsync one more time
  }

  @Test public void doNothingIfAuthenticationFails() {
    when(api.getFormAsync(param.getUrl()))
        .thenReturn(Flowable.just(new BookingForm()));
    when(api.postFormAsync(param.getUrl(), param.postBody()))
        .thenReturn(Flowable.just(new BookingForm()));

    bookingViewModel.loadForm(param).blockingSingle(); // call postFormAsync one time here
    bookingViewModel.observeAuthentication(authenticationViewModel);
    authenticationViewModel.verify("www.google.com").blockingSingle();
    verify(api, times(1)).postFormAsync("url", inputForm); // should call no more postFormAsync
    verify(api, never()).getFormAsync("url"); // should never call getFormAsync
  }

  @Test public void reloadBookingForm() {
    final BookingForm bookingForm = new BookingForm();
    bookingForm.setId("1");
    when(api.postFormAsync(param.getUrl(), param.postBody()))
        .thenReturn(Flowable.just(bookingForm));

    bookingViewModel.loadForm(param).blockingSingle();
    BookingForm actual = bookingViewModel.bookingForm().blockingFirst();
    assertThat(actual.getId()).isEqualTo("1");
  }

  @Test public void getBooking() {
    final BookingForm bookingForm = new BookingForm();
    bookingForm.setId("2");
    when(api.getFormAsync("url"))
        .thenReturn(Flowable.just(bookingForm));

    bookingViewModel.loadForm(Param.create("url")).blockingSingle();
    BookingForm actual = bookingViewModel.bookingForm().blockingFirst();
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
    bookingViewModel.performAction(bookingFormItem).blockingSingle();
    bookingViewModel.nextBookingForm().subscribe(new Consumer<Param>() {
      @Override
      public void accept(Param param) {
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
    final Flowable<BookingForm> observable;
    try {
      observable = bookingViewModel.loadForm(null);
    } catch (NullPointerException e) {
      Assertions.fail("Deal with nullable command param", e);
      return;
    }

    try {
      observable.blockingSingle();
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
        .thenReturn(Flowable.just(bookingForm));
    BookingViewModelImpl bookingViewModel = new BookingViewModelImpl(api);

    bookingViewModel.performAction(bookingForm);
    final TestSubscriber<Boolean> subscriber = bookingViewModel.isDone().test();
    subscriber.assertNoErrors();
    subscriber.assertValueSequence(Arrays.asList(false));
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
        .thenReturn(Flowable.just(bookingForm));
    BookingViewModelImpl bookingViewModel = new BookingViewModelImpl(api);

    bookingViewModel.performAction(bookingForm);
    final TestSubscriber<Boolean> subscriber = new TestSubscriber<>();
    bookingViewModel.isDone().subscribe(subscriber);
    subscriber.assertNoErrors();
    subscriber.assertValueSequence(Arrays.asList(true));
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