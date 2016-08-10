package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.ServiceStop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ExtractServiceExtrasTest {
  @Test
  public void extractTripStops() throws Exception {
    final ArrayList<ServiceStop> stops = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      final ServiceStop stop = new ServiceStop();
      stop.setName("Platform #" + i);
      stop.setCode(String.valueOf(i));
      stops.add(stop);
    }

    final ServiceExtras actual = ExtractServiceExtras.create("3", "6").call(stops);
    assertThat(actual).isNotNull();
    assertThat(actual.platform).isEqualTo("Platform #3");
    assertThat(actual.stops)
        .isNotNull()
        .doesNotContainNull()
        .hasSize(4);

    final List<String> codes = Observable.from(actual.stops)
        .map(new Func1<ServiceStop, String>() {
          @Override
          public String call(ServiceStop stop) {
            return stop.getCode();
          }
        })
        .toList()
        .toBlocking()
        .first();
    assertThat(codes)
        .containsExactly("3", "4", "5", "6");
  }

  @Test
  public void extractTripStopsWithNullOrEmpty() {
    final ServiceExtras actual = ExtractServiceExtras.create("3", "6").call(null);
    assertThat(actual).isNotNull();
    assertThat(actual.platform).isNull();
    assertThat(actual.stops).isNull();
  }
}