package com.skedgo.android.common.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class TripAvailableTest {
  private Gson gson;

  @Before public void before() {
    GsonBuilder builder = new GsonBuilder();
    gson = builder.create();
  }

  @Test public void shouldHaveNotAvailableTrip() throws IOException {
    String tripJson = IOUtils.toString(getClass().
        getResourceAsStream("/mydriver-london-example.json"), Charset.defaultCharset());

    Trip myDriverTrip = gson.fromJson(tripJson, Trip.class);

    assertThat(myDriverTrip).isNotNull();
    assertThat(myDriverTrip.getAvailability()).isNotEqualTo(Availability.Available);
  }

  @Test public void shouldBeNullAvailability() throws IOException {
    assertThat(Availability.fromString(null)).isNull();
  }
}
