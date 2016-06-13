package com.skedgo.android.bookingclient.viewmodel;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import javax.inject.Inject;
import javax.inject.Provider;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TakeEncodedSnapshotCommand implements Command<FragmentActivity, String> {
  private final Provider<TakeSnapshotCommand> takeSnapshotCommand;

  @Inject
  public TakeEncodedSnapshotCommand(@NonNull Provider<TakeSnapshotCommand> takeSnapshotCommand) {
    this.takeSnapshotCommand = takeSnapshotCommand;
  }

  @Override
  public Observable<String> executeAsync(FragmentActivity activity) {
    return takeSnapshotCommand.get()
        .executeAsync(activity)
        .flatMap(new Func1<Bitmap, Observable<String>>() {
          @Override
          public Observable<String> call(Bitmap bitmap) {
            return Observable
                .create(new ToBase64EncodedBitmapOnSubscribe(bitmap))
                .subscribeOn(Schedulers.computation());
          }
        });
  }
}