package com.skedgo.android.tripkit;

import android.test.AndroidTestCase;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.skedgo.android.common.model.Shape;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

public class TransitServiceTest extends AndroidTestCase {
  public void testSerialize() throws IOException {
    final InputStream stream = getContext().getAssets().open("ServiceResponseTest.json");
    final TransitService response = new Gson().fromJson(
        new JsonReader(new InputStreamReader(stream)),
        TransitService.class
    );

    assertThat(response).isNotNull();
    assertThat(response.getType()).isEqualTo("bus");
    assertThat(response.getRealTimeStatus()).isEqualTo("CAPABLE");
    assertThat(response.getShapes())
        .isNotNull()
        .hasSize(1)
        .doesNotContainNull();
    final Shape firstShape = response.getShapes().get(0);
    assertThat(firstShape.getStops())
        .isNotNull()
        .hasSize(26);
  }
}