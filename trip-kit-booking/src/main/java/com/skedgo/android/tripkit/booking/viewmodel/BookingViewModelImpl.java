package com.skedgo.android.tripkit.booking.viewmodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.android.common.rx.Var;
import com.skedgo.android.tripkit.booking.BookingApi;
import com.skedgo.android.tripkit.booking.InputForm;
import com.skedgo.android.tripkit.booking.model.BookingForm;
import com.skedgo.android.tripkit.booking.model.FormField;
import com.skedgo.android.tripkit.booking.model.FormFieldJsonAdapter;
import com.skedgo.android.tripkit.booking.model.FormGroup;
import com.skedgo.android.tripkit.booking.model.LinkFormField;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class BookingViewModelImpl implements BookingViewModel {
  private static final int TIME_RETRY = 3;
  private final BookingApi bookingApi;
  private Var<Param> nextBookingForm = Var.create();
  private Var<BookingForm> bookingForm = Var.create();
  private Var<Boolean> isDone = Var.create();
  private Var<Boolean> isFetching = Var.create(false);
  private Param param;

  public BookingViewModelImpl(BookingApi bookingApi) {
    this.bookingApi = bookingApi;
  }

  public static Gson createGson() {
    return new GsonBuilder().registerTypeAdapter(
        FormField.class,
        new FormFieldJsonAdapter()
    ).create();
  }

  @Override
  public Observable<BookingForm> bookingForm() {
    return bookingForm.observe();
  }

  @Override
  public Observable<Param> nextBookingForm() {
    return nextBookingForm.observe();
  }

  /**
   * Two cases here
   * if postBody == null --> getBooking for the first time
   * else reload the page with the last data
   */
  @Override
  public Observable<BookingForm> loadForm(Param param) {
    if (param == null) {
      return null;
    }
    if (param.getMethod().equals(LinkFormField.METHOD_POST)) {
      this.param = param;
      return bookingApi.postFormAsync(param.getUrl(), param.postBody())
          .retry(TIME_RETRY)
          .observeOn(mainThread())
          .doOnNext(new Action1<BookingForm>() {
            @Override
            public void call(BookingForm bookingFormItem) {
              bookingForm.put(bookingFormItem);
            }
          })
          .doOnRequest(new Action1<Long>() {
            @Override
            public void call(Long value) {
              isFetching.put(true);
            }
          })
          .doOnCompleted(new Action0() {
            @Override
            public void call() {
              isFetching.put(false);
            }
          });
    } else {
      return bookingApi.getFormAsync(param.getUrl())
          .retry(TIME_RETRY)
          .observeOn(mainThread())
          .doOnNext(bookingForm)
          .doOnRequest(new Action1<Long>() {
            @Override
            public void call(Long value) {
              isFetching.put(true);
            }
          })
          .doOnCompleted(new Action0() {
            @Override
            public void call() {
              isFetching.put(false);
            }
          });
    }
  }

  @Override
  public Observable<Boolean> isFetching() {
    return isFetching.observe();
  }

  @Override
  public Observable<Boolean> isDone() {
    return isDone.observe();
  }

  @Override
  public void observeAuthentication(AuthenticationViewModel authenticationViewModel) {
    authenticationViewModel.isSuccessful().subscribe(new Action1<Boolean>() {
      @Override
      public void call(Boolean isSuccessful) {
        if (isSuccessful) {
          loadForm(param).subscribe(Actions.empty(),
                                    new Action1<Throwable>() {
                                      @Override
                                      public void call(Throwable error) {
                                      }
                                    });
        }
      }
    });
  }

  @Override
  public Observable<Boolean> performAction(BookingForm bookingForm) {
    String url = bookingForm.getAction().getUrl();
    InputForm postPody = InputForm.from(bookingForm.getForm());
    if (url == null) {
      if (!canceled(bookingForm)) {
        isDone.put(true);
      } else {
        isDone.put(false);
      }
      return Observable.just(true);
    }
    nextBookingForm.put(ParamImpl.create(bookingForm.getAction(), postPody));
    return Observable.just(false);
  }

  @Override
  public Observable<Boolean> performAction(LinkFormField linkFormField) {
    nextBookingForm.put(ParamImpl.create(linkFormField));
    return Observable.just(false);
  }

  private boolean canceled(BookingForm bookingForm) {
    List<FormGroup> formGroups = bookingForm.getForm();
    if (!CollectionUtils.isEmpty(formGroups)) {
      FormField bookingStatusField = formGroups.get(0).getFields().get(0);
      if (bookingStatusField.getValue().equals("Cancelled")) {
        return true;
      }
    }
    return false;
  }
}