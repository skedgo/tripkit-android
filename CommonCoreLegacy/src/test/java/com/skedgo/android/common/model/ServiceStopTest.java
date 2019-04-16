package com.skedgo.android.common.model;

import com.google.gson.Gson;
import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(TestRunner.class)
public class ServiceStopTest {
  /**
   * To prevent http://crashes.to/s/a42b1958de6.
   */
  @Test
  public void serializeAndDeserializeByGson() {
    final ServiceStop expected = new ServiceStop();
    expected.setCode("SomeCode");
    expected.setArrivalTime(345L);
    expected.departureSecs().put(123L);
    expected.setType(StopType.BUS);

    final Gson gson = new Gson();
    final ServiceStop actual = gson.fromJson(gson.toJson(expected), ServiceStop.class);

    assertThat(actual.getCode()).isEqualTo(expected.getCode());
    assertThat(actual.getArrivalTime()).isEqualTo(expected.getArrivalTime());
    assertThat(actual.departureSecs().value()).isEqualTo(expected.departureSecs().value());
    assertThat(actual.getType()).isEqualTo(expected.getType());
  }
}