package com.skedgo.android.common.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TransportModeTest {
  @Test public void serializedNames() {
    final TransportMode mode = new TransportMode();
    mode.setId("Some id");
    mode.setURL("skedgo.com");
    mode.setTitle("Some title");
    mode.setIconId("Some icon id");
    mode.setDarkIcon("Some dark icon");
    mode.setImplies(new ArrayList<>(Arrays.asList("Aha", "Uhu", "Huh?")));
    mode.setColor(new ServiceColor(1, 2, 3));

    final JsonObject json = new Gson().toJsonTree(mode).getAsJsonObject();
    assertThat(json).isNotNull();
    assertThat(json.has("id")).isTrue();
    assertThat(json.has("URL")).isTrue();
    assertThat(json.has("title")).isTrue();
    assertThat(json.has("implies")).isTrue();
    assertThat(json.has("required")).isTrue();
    assertThat(json.has("icon")).isTrue();
    assertThat(json.has("darkIcon")).isTrue();
    assertThat(json.has("color")).isTrue();
  }

  @Test public void shouldBeEqual() {
    final TransportMode mode0 = TransportMode.fromId("bus");
    final TransportMode mode1 = TransportMode.fromId("bus");

    assertThat(mode0).isEqualTo(mode1);
    assertThat(mode1).isEqualTo(mode0);
  }

  @Test public void shouldNotBeEqual() {
    final TransportMode bus = TransportMode.fromId("bus");
    final TransportMode car = TransportMode.fromId("car");

    assertThat(bus).isNotEqualTo("Awesome!");
    assertThat(bus).isNotEqualTo(car);
    assertThat(car).isNotEqualTo(bus);
  }

  @Test public void newFromId() {
    final TransportMode bus = TransportMode.fromId("bus");
    assertThat(bus.getId()).isEqualTo("bus");
  }

  @Test public void parcel() {
    final TransportMode mode = new TransportMode();
    mode.setId("Some id");
    mode.setURL("skedgo.com");
    mode.setTitle("Some title");
    mode.setIconId("Some icon id");
    mode.setDarkIcon("Some dark icon");
    mode.setImplies(new ArrayList<>(Arrays.asList("Aha", "Uhu", "Huh?")));
    mode.setColor(new ServiceColor(1, 2, 3));

    final TransportMode actual = TransportMode.CREATOR.createFromParcel(Utils.parcel(mode));

    assertThat(actual).isNotNull();
    assertEquals(mode.getId(), actual.getId());
    assertEquals(mode.getURL(), actual.getURL());
    assertEquals(mode.getTitle(), actual.getTitle());
    assertEquals(mode.getIconId(), actual.getIconId());
    assertEquals(mode.getDarkIcon(), actual.getDarkIcon());
    assertThat(actual.getColor()).isEqualTo(mode.getColor());
    assertThat(actual.getImplies()).containsExactlyElementsOf(mode.getImplies());
  }
}