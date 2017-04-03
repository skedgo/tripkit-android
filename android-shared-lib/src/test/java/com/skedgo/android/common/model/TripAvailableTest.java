package com.skedgo.android.common.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class TripAvailableTest {

  private Gson gson;

  @Before public void before() {
    GsonBuilder builder = new GsonBuilder();
    gson = builder.create();
  }

  @Test public void shoudHaveNotAvailableTrop() throws IOException{

    String tripJson = IOUtils.toString(getClass().
        getResourceAsStream("/mydriver-london-example.json"), Charset.defaultCharset());

    Trip myDriverTrip = gson.fromJson(tripJson, Trip.class);

    assertThat(myDriverTrip).isNotNull();
    assertThat(myDriverTrip.getAvailability()).isNotEqualTo(Availability.Available);

  }


}