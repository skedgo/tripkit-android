package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TransportMode;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

final class Utils {
  private Utils() {}

  @NonNull
  static Observable.Transformer<Region, Location> getCities() {
    return new Observable.Transformer<Region, Location>() {
      @Override
      public Observable<Location> call(Observable<Region> observable) {
        return observable
            .flatMap(new Func1<Region, Observable<Region.City>>() {
              @Override
              public Observable<Region.City> call(Region region) {
                final ArrayList<Region.City> cities = region.getCities();
                if (cities != null) {
                  return Observable.from(cities);
                } else {
                  return Observable.empty();
                }
              }
            })
            .map(new Func1<Region.City, Location>() {
              @Override
              public Location call(Region.City city) {
                return city;
              }
            });
      }
    };
  }

  /**
   * @return True for either null or "" string or string having only spaces.
   * Also true if the city name contains the keyword. Otherwise, false.
   */
  @NonNull
  static Func1<Location, Boolean> matchCityName(@Nullable final String name) {
    final String lowerCaseName = name != null ? name.toLowerCase() : null;
    return new Func1<Location, Boolean>() {
      @Override
      public Boolean call(Location city) {
        final String name = city.getName();
        return isNullOrEmpty(lowerCaseName) ||
            (name != null && name.toLowerCase().contains(lowerCaseName));
      }
    };
  }

  static boolean isNullOrEmpty(CharSequence s) {
    return s == null || TextUtils.getTrimmedLength(s) == 0;
  }

  @NonNull
  static Func1<Map<String, TransportMode>, List<TransportMode>> findModesByIds(final List<String> modeIds) {
    return new Func1<Map<String, TransportMode>, List<TransportMode>>() {
      @Override
      public List<TransportMode> call(Map<String, TransportMode> modeMap) {
        final List<TransportMode> modes = new ArrayList<>(modeIds.size());
        for (String modeId : modeIds) {
          final TransportMode mode = modeMap.get(modeId);
          if (mode != null) {
            modes.add(mode);
          }
        }

        return modes;
      }
    };
  }

  @NonNull
  static Func1<List<TransportMode>, Map<String, TransportMode>> toModeMap() {
    return new Func1<List<TransportMode>, Map<String, TransportMode>>() {
      @Override
      public Map<String, TransportMode> call(List<TransportMode> modes) {
        final HashMap<String, TransportMode> modeMap = new HashMap<>();
        if (modes != null) {
          for (TransportMode mode : modes) {
            modeMap.put(mode.getId(), mode);
          }
        }

        return modeMap;
      }
    };
  }

  static <T> Func1<List<T>, Boolean> isNotEmpty() {
    return new Func1<List<T>, Boolean>() {
      @Override public Boolean call(List<T> items) {
        return CollectionUtils.isNotEmpty(items);
      }
    };
  }
}