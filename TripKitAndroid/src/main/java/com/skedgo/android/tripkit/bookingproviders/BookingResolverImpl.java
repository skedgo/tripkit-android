package com.skedgo.android.tripkit.bookingproviders;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skedgo.android.tripkit.BookingAction;
import com.skedgo.android.tripkit.ExternalActionParams;
import com.skedgo.android.tripkit.Geocodable;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public final class BookingResolverImpl implements BookingResolver {
  private Map<String, BookingResolver> resolverMap;

  public BookingResolverImpl(
      @NonNull Resources resources,
      @NonNull final PackageManager packageManager,
      @NonNull Geocodable geocoderFactory) {
    final Func1<String, Boolean> isPackageInstalled = new Func1<String, Boolean>() {
      @Override public Boolean call(String packageName) {
        try {
          packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
          return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
      }
    };

    final Func1<String, Intent> getAppIntent = new Func1<String, Intent>() {
      @Override public Intent call(String packageName) {
        return packageManager.getLaunchIntentForPackage(packageName);
      }
    };

    resolverMap = new HashMap<>(8);
    resolverMap.put("gocatch", new GoCatchBookingResolver(resources, isPackageInstalled, geocoderFactory));
    resolverMap.put("ingogo", new IngogoBookingResolver(resources, isPackageInstalled));
    resolverMap.put("mtaxi", new MTaxiBookingResolver(isPackageInstalled, getAppIntent));
    resolverMap.put("uber", new UberBookingResolver(isPackageInstalled, getAppIntent));
    resolverMap.put("lyft", new LyftBookingResolver(resources, isPackageInstalled));
    resolverMap.put("flitways", new FlitWaysBookingResolver(geocoderFactory));
    resolverMap.put("tel:", new TelBookingResolver(resources));
    resolverMap.put("sms:", new SmsBookingResolver());
    resolverMap.put("http", new WebBookingResolver(resources));
  }

  @Override
  public Observable<BookingAction> performExternalActionAsync(ExternalActionParams params) {
    final String externalAction = params.action();
    final BookingResolver resolver = getBookingResolver(externalAction);
    if (resolver != null) {
      return resolver.performExternalActionAsync(params);
    } else {
      return Observable.error(new UnsupportedOperationException("Strange action: " + externalAction));
    }
  }

  @Nullable @Override public String getTitleForExternalAction(String externalAction) {
    BookingResolver resolver = getBookingResolver(externalAction);
    if (resolver != null) {
      return resolver.getTitleForExternalAction(externalAction);
    } else {
      return null;
    }
  }

  private BookingResolver getBookingResolver(String externalAction) {
    if (externalAction.startsWith("lyft")) {
      return resolverMap.get("lyft");
    } else if (externalAction.startsWith("http")) {
      return resolverMap.get("http");
    } else if (externalAction.startsWith("tel:")) {
      return resolverMap.get("tel:");
    } else if (externalAction.startsWith("sms:")) {
      return resolverMap.get("sms:");
    } else if (resolverMap.containsKey(externalAction)) {
      return resolverMap.get(externalAction);
    } else {
      return null;
    }
  }
}