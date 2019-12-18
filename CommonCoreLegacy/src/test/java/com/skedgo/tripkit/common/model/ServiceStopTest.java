package com.skedgo.tripkit.common.model;

import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ServiceStopTest {
  /**
   * To prevent http://crashes.to/s/a42b1958de6.
   */
  @Test
  public void serializeAndDeserializeByGson() {
    final ServiceStop expected = new ServiceStop();
    expected.setCode("SomeCode");
    expected.setArrivalTime(345L);
    expected.setDepartureSecs(123L);
    expected.setType(StopType.BUS);

    final Gson gson = new Gson();
    final ServiceStop actual = gson.fromJson(gson.toJson(expected), ServiceStop.class);

    assertThat(actual.getCode()).isEqualTo(expected.getCode());
    assertThat(actual.getArrivalTime()).isEqualTo(expected.getArrivalTime());
    assertThat(actual.departureSecs()).isEqualTo(expected.departureSecs());
    assertThat(actual.getType()).isEqualTo(expected.getType());
  }
}