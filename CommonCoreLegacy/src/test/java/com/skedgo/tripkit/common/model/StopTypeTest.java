package com.skedgo.tripkit.common.model;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static com.skedgo.tripkit.common.model.stop.StopType.BUS;
import static com.skedgo.tripkit.common.model.stop.StopType.CABLECAR;
import static com.skedgo.tripkit.common.model.stop.StopType.FERRY;
import static com.skedgo.tripkit.common.model.stop.StopType.MONORAIL;
import static com.skedgo.tripkit.common.model.stop.StopType.PARKING;
import static com.skedgo.tripkit.common.model.stop.StopType.SUBWAY;
import static com.skedgo.tripkit.common.model.stop.StopType.TAXI;
import static com.skedgo.tripkit.common.model.stop.StopType.TRAIN;
import static com.skedgo.tripkit.common.model.stop.StopType.TRAM;
import static com.skedgo.tripkit.common.model.stop.StopType.from;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class StopTypeTest {
    @Test
    public void shouldConvertIntoBus() {
        assertThat(from("bus")).isEqualTo(BUS);
    }

    @Test
    public void shouldConvertIntoBusEvenIfUppercaseString() {
        assertThat(from("BUS")).isEqualTo(BUS);
    }

    @Test
    public void shouldConvertIntoTrain() {
        assertThat(from("train")).isEqualTo(TRAIN);
    }

    @Test
    public void shouldConvertIntoTrainEvenIfUppercaseString() {
        assertThat(from("TRAIN")).isEqualTo(TRAIN);
    }

    @Test
    public void shouldConvertIntoFerry() {
        assertThat(from("ferry")).isEqualTo(FERRY);
    }

    @Test
    public void shouldConvertIntoFerryEvenIfUppercaseString() {
        assertThat(from("FERRY")).isEqualTo(FERRY);
    }

    @Test
    public void shouldConvertIntoMonorail() {
        assertThat(from("monorail")).isEqualTo(MONORAIL);
    }

    @Test
    public void shouldConvertIntoMonorailEvenIfUppercaseString() {
        assertThat(from("MONORAIL")).isEqualTo(MONORAIL);
    }

    @Test
    public void shouldConvertIntoSubway() {
        assertThat(from("subway")).isEqualTo(SUBWAY);
    }

    @Test
    public void shouldConvertIntoSubwayEvenIfUppercaseString() {
        assertThat(from("SUBWAY")).isEqualTo(SUBWAY);
    }

    @Test
    public void shouldConvertIntoTaxi() {
        assertThat(from("taxi")).isEqualTo(TAXI);
    }

    @Test
    public void shouldConvertIntoTaxiEvenIfUppercaseString() {
        assertThat(from("TAXI")).isEqualTo(TAXI);
    }

    @Test
    public void shouldConvertIntoParking() {
        assertThat(from("parking")).isEqualTo(PARKING);
    }

    @Test
    public void shouldConvertIntoParkingEvenIfUppercaseString() {
        assertThat(from("PARKING")).isEqualTo(PARKING);
    }

    @Test
    public void shouldConvertIntoTram() {
        assertThat(from("tram")).isEqualTo(TRAM);
    }

    @Test
    public void shouldConvertIntoTramEvenIfUppercaseString() {
        assertThat(from("TRAM")).isEqualTo(TRAM);
    }

    @Test
    public void shouldConvertIntoCableCar() {
        assertThat(from("cablecar")).isEqualTo(CABLECAR);
    }

    @Test
    public void shouldConvertIntoCableCarEvenIfUppercaseString() {
        assertThat(from("CABLECAR")).isEqualTo(CABLECAR);
    }

    @Test
    public void shouldConvertIntoNullIfInvalidString() {
        assertThat(from("I'm not an enum!")).isNull();
    }
}