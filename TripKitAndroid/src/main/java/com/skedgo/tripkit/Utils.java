package com.skedgo.tripkit;

import android.text.TextUtils;

import com.skedgo.tripkit.common.model.location.Location;
import com.skedgo.tripkit.common.model.region.Region;
import com.skedgo.tripkit.common.model.TransportMode;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

final class Utils {
    private Utils() {
    }

    @NonNull
    static ObservableTransformer<Region, Location> getCities() {
        return new ObservableTransformer<Region, Location>() {
            @Override
            public Observable<Location> apply(Observable<Region> observable) {
                return observable
                    .flatMap((Function<Region, Observable<Region.City>>) region -> {
                        final ArrayList<Region.City> cities = region.getCities();
                        if (cities != null) {
                            return Observable.fromIterable(cities);
                        } else {
                            return Observable.empty();
                        }
                    })
                    .map(city -> city);
            }
        };
    }

    /**
     * @return True for either null or "" string or string having only spaces.
     * Also true if the city name contains the keyword. Otherwise, false.
     */
    @NonNull
    static Predicate<Location> matchCityName(@Nullable final String name) {
        final String lowerCaseName = name != null ? name.toLowerCase() : null;
        return city -> {
            final String name1 = city.getName();
            return isNullOrEmpty(lowerCaseName) ||
                (name1 != null && name1.toLowerCase().contains(lowerCaseName));
        };
    }

    static boolean isNullOrEmpty(CharSequence s) {
        return s == null || TextUtils.getTrimmedLength(s) == 0;
    }

    @NonNull
    static Function<Map<String, TransportMode>, List<TransportMode>> findModesByIds(final List<String> modeIds) {
        return modeMap -> {
            final List<TransportMode> modes = new ArrayList<>(modeIds.size());
            for (String modeId : modeIds) {
                final TransportMode mode = modeMap.get(modeId);
                if (mode != null) {
                    modes.add(mode);
                }
            }

            return modes;
        };
    }

    @NonNull
    static Function<List<TransportMode>, Map<String, TransportMode>> toModeMap() {
        return new Function<List<TransportMode>, Map<String, TransportMode>>() {
            @Override
            public Map<String, TransportMode> apply(List<TransportMode> modes) {
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

    static <T> Predicate<List<T>> isNotEmpty() {
        return new Predicate<List<T>>() {
            @Override
            public boolean test(List<T> items) {
                return CollectionUtils.isNotEmpty(items);
            }
        };
    }
}