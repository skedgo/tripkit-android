package com.skedgo.android.tripkit;

import android.text.TextUtils;

import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TransportMode;

import org.assertj.core.api.Condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.assertj.core.api.Assertions.assertThat;

public final class RegionAssertionUtils {
  private RegionAssertionUtils() {}

  public static void assertTransportModes(Collection<TransportMode> modes) {
    assertThat(modes)
        .describedAs("Should contain more than one mode")
        .isNotNull().isNotEmpty()
        .describedAs("Should contain no null mode")
        .doesNotContainNull()
        .describedAs("Mode title should be non-null")
        .extractingResultOf("getTitle").doesNotContainNull();
    for (TransportMode mode : modes) {
      assertThat(mode.getId())
          .describedAs("Mode '%s' should have non-null id", mode.getTitle())
          .isNotNull().isNotEmpty();
    }
  }

  public static void assertRegions(List<Region> regions) {
    assertThat(regions)
        .isNotNull()
        .describedAs("Should contain more than one region")
        .isNotEmpty()
        .describedAs("Should contain no null region")
        .doesNotContainNull()
        .describedAs("Should have at least one endpoint")
        .doNotHave(new Condition<Region>() {
          @Override
          public boolean matches(Region region) {
            return isEmpty(region.getURLs());
          }
        })
        .describedAs("Should have a polygon")
        .doNotHave(new Condition<Region>() {
          @Override
          public boolean matches(Region region) {
            return region.getEncodedPolyline() == null;
          }
        })
        .describedAs("Should contain its cities")
        .doNotHave(new Condition<Region>() {
          @Override
          public boolean matches(final Region region) {
            final ArrayList<Region.City> cities = region.getCities();
            return cities != null && isNotEmpty(
                Observable.from(cities)
                    .filter(new Func1<Region.City, Boolean>() {
                      @Override
                      public Boolean call(Region.City city) {
                        return !region.containsLocation(city);
                      }
                    })
                    .toList()
                    .toBlocking()
                    .firstOrDefault(null));
          }
        })
        .describedAs("Should contain non-null name")
        .doNotHave(new Condition<Region>() {
          @Override
          public boolean matches(Region region) {
            return TextUtils.isEmpty(region.getName());
          }
        })
        .describedAs("Should contain non-null timezone")
        .doNotHave(new Condition<Region>() {
          @Override
          public boolean matches(Region region) {
            return TextUtils.isEmpty(region.getTimezone());
          }
        });
  }
}