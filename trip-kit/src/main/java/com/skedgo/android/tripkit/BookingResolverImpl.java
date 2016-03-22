package com.skedgo.android.tripkit;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public final class BookingResolverImpl implements BookingResolver {

  private Map<String, ExternalActionStrategy> externalActionsMap;

  public BookingResolverImpl(
      Resources resources, final PackageManager packageManager,
      @NonNull Func1<ReverseGeocodingParams, Observable<String>> reverseGeocoderFactory) {

    // init external actions strategies
    Func1<String, Boolean> isPackageInstalled = new Func1<String, Boolean>() {
      @Override public Boolean call(String packageName) {
        try {
          packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
          return true;
        } catch (PackageManager.NameNotFoundException e) {
          // Ignored.
        }
        return false;
      }
    };

    externalActionsMap = new HashMap<>(8);
    externalActionsMap.put("gocatch", new ActionGoCatchStrategy(resources));
    externalActionsMap.put("uber", new ActionUberStrategy(resources, isPackageInstalled));
    externalActionsMap.put("ingogo", new ActionIngogoStrategy(resources));
    externalActionsMap.put("lyft", new ActionLyftStrategy(resources, isPackageInstalled));
    externalActionsMap.put("flitways", new ActionFlitWaysStrategy(resources, reverseGeocoderFactory));
    externalActionsMap.put("tel:", new ActionTelStrategy(resources));
    externalActionsMap.put("sms:", new ActionSmsStrategy(resources));
    externalActionsMap.put("http", new ActionHttpStrategy(resources));

  }

  @Override public Observable<BookingAction> performExternalActionAsync(
      ExternalActionParams params) {

    final String externalAction = params.action();

    ExternalActionStrategy externalActionStrategy = getExternalActionStrategy(externalAction);

    if (externalActionStrategy != null) {
      return externalActionStrategy.performExternalActionAsync(params);
    } else {
      return Observable.error(new UnsupportedOperationException());
    }
  }

  @Nullable @Override public String getTitleForExternalAction(String externalAction) {

    ExternalActionStrategy externalActionStrategy = getExternalActionStrategy(externalAction);

    if (externalActionStrategy != null) {
      return externalActionStrategy.getTitleForExternalAction(externalAction);
    } else {
      return null;
    }
  }

  private ExternalActionStrategy getExternalActionStrategy(String externalAction) {
    if (externalAction.startsWith("lyft")) {
      return externalActionsMap.get("lyft");
    } else if (externalAction.startsWith("http")) {
      return externalActionsMap.get("http");
    } else if (externalAction.startsWith("tel:")) {
      return externalActionsMap.get("tel:");
    } else if (externalAction.startsWith("sms:")) {
      return externalActionsMap.get("sms:");
    } else if (externalActionsMap.containsKey(externalAction)) {
      return externalActionsMap.get(externalAction);
    } else {
      return null;
    }
  }

}