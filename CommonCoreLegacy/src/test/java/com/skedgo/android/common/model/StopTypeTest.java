package com.skedgo.android.common.model;

import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.skedgo.android.common.model.StopType.BUS;
import static com.skedgo.android.common.model.StopType.CABLECAR;
import static com.skedgo.android.common.model.StopType.FERRY;
import static com.skedgo.android.common.model.StopType.MONORAIL;
import static com.skedgo.android.common.model.StopType.PARKING;
import static com.skedgo.android.common.model.StopType.SUBWAY;
import static com.skedgo.android.common.model.StopType.TAXI;
import static com.skedgo.android.common.model.StopType.TRAIN;
import static com.skedgo.android.common.model.StopType.TRAM;
import static com.skedgo.android.common.model.StopType.from;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(TestRunner.class)
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