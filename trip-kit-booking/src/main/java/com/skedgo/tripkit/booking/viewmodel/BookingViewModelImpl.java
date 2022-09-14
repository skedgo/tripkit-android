package com.skedgo.tripkit.booking.viewmodel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.tripkit.common.rx.Var;
import com.skedgo.tripkit.booking.BookingForm;
import com.skedgo.tripkit.booking.BookingService;
import com.skedgo.tripkit.booking.FormField;
import com.skedgo.tripkit.booking.FormFieldJsonAdapter;
import com.skedgo.tripkit.booking.FormGroup;
import com.skedgo.tripkit.booking.InputForm;
import com.skedgo.tripkit.booking.LinkFormField;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.LongConsumer;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

public class BookingViewModelImpl implements BookingViewModel {
  private final BookingService bookingService;
  private Var<Param> nextBookingForm = Var.create();
  private Var<BookingForm> bookingForm = Var.create();
  private Var<Boolean> isDone = Var.create();
  private Var<Boolean> isFetching = Var.create(false);
  private Param param;

  public BookingViewModelImpl(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  public static Gson createGson() {
    return new GsonBuilder().registerTypeAdapter(
        FormField.class,
        new FormFieldJsonAdapter()
    ).create();
  }

  @Override
  public Flowable<BookingForm> bookingForm() {
    return bookingForm.observe();
  }

  @Override
  public Flowable<Param> nextBookingForm() {
    return nextBookingForm.observe();
  }

  /**
   * Two cases here
   * if postBody == null --> getBooking for the first time
   * else reload the page with the last data
   */
  @Override
  public Flowable<BookingForm> loadForm(Param param) {
    if (param == null) {
      return null;
    }
    if (param.getMethod().equals(LinkFormField.METHOD_POST)) {
      this.param = param;
      return bookingService.postFormAsync(param.getUrl(), param.postBody())
          .observeOn(mainThread())
          .doOnNext(new Consumer<BookingForm>() {
            @Override
            public void accept(BookingForm bookingFormItem) {
              // Server can return an empty form
              if (bookingFormItem != null) {
                bookingForm.put(bookingFormItem);
              }
            }
          })
          .doOnRequest(new LongConsumer() {
            @Override
            public void accept(long t) throws Exception {
              isFetching.put(true);

            }
          })
          .doOnComplete(new Action() {
            @Override
            public void run() {
              isFetching.put(false);
            }
          });
    } else {
      return bookingService.getFormAsync(param.getUrl())
          .observeOn(mainThread())
              .doOnNext(new Consumer<BookingForm>() {
                @Override
                public void accept(BookingForm bookingFormItem) throws Exception {
                  if (bookingForm != null) {
                      bookingForm.put(bookingFormItem);
                  }
                }
              })
          .doOnRequest(new LongConsumer() {
            @Override
            public void accept(long value) {
              isFetching.put(true);
            }
          })
          .doOnComplete(new Action() {
            @Override
            public void run() {
              isFetching.put(false);
            }
          });
    }
  }

  @Override
  public Flowable<Boolean> isFetching() {
    return isFetching.observe();
  }

  @Override public Param paramFrom(BookingForm form) {
    return Param.create(form);
  }

  @Override
  public Flowable<Boolean> isDone() {
    return isDone.observe();
  }

  @Override
  public void observeAuthentication(AuthenticationViewModel authenticationViewModel) {
    authenticationViewModel.isSuccessful().subscribe(new Consumer<Boolean>() {
      @Override
      public void accept(Boolean isSuccessful) {
        if (isSuccessful) {
          loadForm(param).subscribe(
              unused -> {},
              new Consumer<Throwable>() {
                @Override
                public void accept(Throwable error) {
                }
              }
          );
        }
      }
    });
  }

  @Override
  public Flowable<Boolean> performAction(BookingForm bookingForm) {
    String url = bookingForm.getAction().getUrl();
    InputForm postPody = InputForm.from(bookingForm.getForm());
    if (url == null) {
      if (!canceled(bookingForm)) {
        isDone.put(true);
      } else {
        isDone.put(false);
      }
      return Flowable.just(true);
    }
    nextBookingForm.put(Param.create(bookingForm.getAction(), postPody));
    return Flowable.just(false);
  }

  @Override
  public Flowable<Boolean> performAction(LinkFormField linkFormField) {
    nextBookingForm.put(Param.create(linkFormField));
    return Flowable.just(false);
  }

  private boolean canceled(BookingForm bookingForm) {
    List<FormGroup> formGroups = bookingForm.getForm();
    if (!CollectionUtils.isEmpty(formGroups)) {
      FormField bookingStatusField = formGroups.get(0).getFields().get(0);
      if ("Cancelled".equals(bookingStatusField.getValue())) {
        return true;
      }
    }
    return false;
  }
}