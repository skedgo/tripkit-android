package com.skedgo.android.common.model;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class QueryTest {
  @Test public void shouldParcel() {
    final Region expectedRegion = new Region();
    String expectedRegionName = "Fake region";
    expectedRegion.setName(expectedRegionName);
    expectedRegion.setURLs(new ArrayList<>(Arrays.asList("Fake URL 1", "Fake URL 2")));

    final Query expected = new Query();
    expected.setRegion(expectedRegion);
    expected.setFromLocation(new Location(1.0, 2.0));
    expected.setToLocation(new Location(3.0, 4.0));
    expected.setTransportModeIds(new ArrayList<>(Arrays.asList("ps_awesome", "pt_cool")));
    expected.setMaxWalkingTime(25);

    final Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    final Query actual = Query.CREATOR.createFromParcel(parcel);

    assertThat(actual.getRegion()).isNotNull();
    assertThat(actual.getRegion().getName()).isEqualTo(expectedRegionName);
    assertThat(actual.getRegion().getURLs()).containsExactlyElementsOf(expectedRegion.getURLs());

    assertThat(actual.getTransportModeIds())
        .isNotNull()
        .containsExactly("ps_awesome", "pt_cool");
    assertThat(actual.getFromLocation()).isNotNull();
    assertThat(actual.getToLocation()).isNotNull();
    assertThat(actual.getMaxWalkingTime()).isEqualTo(expected.getMaxWalkingTime());
    assertThat(actual.uuid()).isEqualTo(expected.uuid());
  }

  @Test public void shouldClone() {
    final Query query = new Query();
    query.setMaxWalkingTime(10);
    final Query clone = query.clone();

    assertThat(clone.getMaxWalkingTime())
        .isEqualTo(query.getMaxWalkingTime());

    // To prevent https://skedgo.uservoice.com/admin/tickets/7729.
    assertThat(clone.uuid()).isNotEqualTo(query.uuid());
  }
}