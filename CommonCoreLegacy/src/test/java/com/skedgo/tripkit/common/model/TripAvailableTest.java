package com.skedgo.tripkit.common.model;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.charset.Charset;

import com.skedgo.tripkit.routing.Availability;
import com.skedgo.tripkit.routing.Trip;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class TripAvailableTest {
    private Gson gson = new Gson();

    @Test
    public void shouldHaveNotAvailableTrip() throws IOException {
        String tripJson = IOUtils.toString(getClass().
            getResourceAsStream("/mydriver-london-example.json"), Charset.defaultCharset());

        Trip myDriverTrip = gson.fromJson(tripJson, Trip.class);

        assertThat(myDriverTrip).isNotNull();
        assertThat(myDriverTrip.getAvailability()).isNotEqualTo(Availability.Available);
    }
}
