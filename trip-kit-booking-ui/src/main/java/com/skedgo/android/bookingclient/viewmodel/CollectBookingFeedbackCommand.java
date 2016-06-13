package com.skedgo.android.bookingclient.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.uservoice.uservoicesdk.Config;

import javax.inject.Inject;
import javax.inject.Provider;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class CollectBookingFeedbackCommand implements Command<CollectBookingFeedbackCommand.Param, Config> {
  private final Context context;
  private final Provider<TakeEncodedSnapshotCommand> takeEncodedSnapshotCommand;

  @Inject
  public CollectBookingFeedbackCommand(@NonNull Context context,
                                       @NonNull Provider<TakeEncodedSnapshotCommand> takeEncodedSnapshotCommand) {
    this.context = context;
    this.takeEncodedSnapshotCommand = takeEncodedSnapshotCommand;
  }

  @Override
  public Observable<Config> executeAsync(final Param param) {
    return takeEncodedSnapshotCommand.get()
        .executeAsync(param.activity)
        .onErrorResumeNext(Observable.<String>just(null))
        .flatMap(new Func1<String, Observable<Config>>() {
          @Override
          public Observable<Config> call(final String encodedSnapshot) {
            return Observable.create(new Observable.OnSubscribe<Config>() {
              @Override
              public void call(Subscriber<? super Config> subscriber) {

                // TODO: move this outside this module.
                                /*final Config config = Problem.with(context)
                                        .onScreen(param.activity.getClass().getSimpleName())
                                        .attachScreenshot("screenshot.png", encodedSnapshot)
                                        .toConfig();
                                subscriber.onNext(config);
                                subscriber.onCompleted();*/
              }
            });
          }
        });
  }

  public final static class Param {
    public final FragmentActivity activity;

    public Param(@NonNull FragmentActivity activity) {
      this.activity = activity;
    }
  }
}