package com.skedgo.tripkit.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class LowercaseEnumTypeAdapterFactoryTest {
    private static final String KEY_SPEED_TYPE = "speedType";
    private Gson gson;

    @Before
    public void setUp() {
        gson = new GsonBuilder()
            .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
            .create();
    }

    @Test
    public void valueShouldBeSlowInLowercase() {
        assertEnumSerialization(SpeedType.SLOW, "slow");
    }

    @Test
    public void valueShouldBeMediumInLowercase() {
        assertEnumSerialization(SpeedType.MEDIUM, "medium");
    }

    @Test
    public void valueShouldBeFastInLowercase() {
        assertEnumSerialization(SpeedType.FAST, "fast");
    }

    @Test
    public void valueShouldBeNull() {
        final JsonElement json = gson.toJsonTree(new Sample(null));
        assertThat(json.getAsJsonObject().has(KEY_SPEED_TYPE)).isFalse();
    }

    private void assertEnumSerialization(SpeedType speedType, String expectedValue) {
        final JsonElement json = gson.toJsonTree(new Sample(speedType));
        final String value = json.getAsJsonObject().getAsJsonPrimitive(KEY_SPEED_TYPE).getAsString();
        assertThat(value).isEqualTo(expectedValue);
    }

    enum SpeedType {SLOW, MEDIUM, FAST}

    private static class Sample {
        private final SpeedType speedType;

        private Sample(SpeedType speedType) {
            this.speedType = speedType;
        }
    }
}