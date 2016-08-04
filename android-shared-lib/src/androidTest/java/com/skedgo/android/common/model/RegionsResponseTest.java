package com.skedgo.android.common.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class RegionsResponseTest {
  @Test
  public void shouldHasCorrectSerializeNames() {
    final RegionsResponse response = new RegionsResponse();
    response.setRegions(new ArrayList<Region>());
    response.setTransportModeMap(new HashMap<String, TransportMode>());

    final JsonObject jsonResponse = new Gson().toJsonTree(response).getAsJsonObject();
    assertThat(jsonResponse.has("regions")).isTrue();
    assertThat(jsonResponse.has("modes")).isTrue();
  }

  /**
   * After parsing {@link RegionsResponse}, id in {@link TransportMode} is still null
   * as modes in the response are represented by a map whose key is mode id.
   */
  @Test
  public void shouldCorrectModeIdsBeforeReturningModes() {
    final HashMap<String, TransportMode> modeMap = new HashMap<>();
    final TransportMode a = new TransportMode();
    modeMap.put("a", a);
    final TransportMode b = new TransportMode();
    modeMap.put("b", b);

    final RegionsResponse response = new RegionsResponse();
    response.setTransportModeMap(modeMap);
    final Collection<TransportMode> modes = response.getTransportModes();
    assertThat(modes).extractingResultOf("getId").containsExactly("a", "b");
  }
}