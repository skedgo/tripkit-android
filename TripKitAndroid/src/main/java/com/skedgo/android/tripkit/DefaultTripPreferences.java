package com.skedgo.android.tripkit;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import util.ReactiveSharedPreferencesKt;

public final class DefaultTripPreferences implements TripPreferences {
  private final SharedPreferences preferences;

  public DefaultTripPreferences(@NonNull SharedPreferences preferences) {
    this.preferences = preferences;
  }

  @Override public boolean isConcessionPricingPreferred() {
    return preferences.getBoolean("isConcessionPricingPreferred", false);
  }

  @Override public void setConcessionPricingPreferred(boolean isConcessionPricingPreferred) {
    preferences.edit()
        .putBoolean("isConcessionPricingPreferred", isConcessionPricingPreferred)
        .apply();
  }

  @Override public boolean isWheelchairPreferred() {
    return preferences.getBoolean("isWheelchairPreferred", false);
  }

  @Override public Observable<Boolean> hasWheelchairInformation() {
    return Observable
        .fromCallable(new Callable<Boolean>() {
          @Override public Boolean call() throws Exception {
            return preferences.getBoolean("isWheelchairPreferred", false);
          }
        })
        .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
          @Override public Observable<?> call(Observable<? extends Void> observable) {
            return ReactiveSharedPreferencesKt
                .onChanged(preferences)
                .filter(new Func1<String, Boolean>() {
                  @Override public Boolean call(String key) {
                    return key.equals("isWheelchairPreferred");
                  }
                });
          }
        })
        .subscribeOn(Schedulers.io());
  }

  @Override public void setWheelchairPreferred(boolean isWheelchairPreferred) {
    preferences.edit()
        .putBoolean("isWheelchairPreferred", isWheelchairPreferred)
        .apply();
  }
}