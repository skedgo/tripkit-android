package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static com.skedgo.android.tripkit.RouteOptions.CYCLING_SPEED_AVERAGE;
import static com.skedgo.android.tripkit.RouteOptions.CYCLING_SPEED_FAST;
import static com.skedgo.android.tripkit.RouteOptions.WALKING_SPEED_AVERAGE;
import static com.skedgo.android.tripkit.RouteOptions.WALKING_SPEED_SLOW;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RouteOptionsTest {
  @Test
  public void shouldBuildRouteOptionsProperly_1() {
    final long arriveByInMillis = System.currentTimeMillis();
    final int expectedTransferTime = 5;
    final int expectedMaxWalkingTime = 10;
    final RouteOptions routeOptions = new RouteOptions
        .Builder(new Location(), new Location())
        .walkingSpeed(WALKING_SPEED_SLOW)
        .cyclingSpeed(CYCLING_SPEED_AVERAGE)
        .arriveBy(arriveByInMillis)
        .transferTime(expectedTransferTime)
        .maxWalkingTime(expectedMaxWalkingTime)
        .build();
    assertThat(routeOptions.getWalkingSpeed()).isEqualTo(WALKING_SPEED_SLOW);
    assertThat(routeOptions.getCyclingSpeed()).isEqualTo(CYCLING_SPEED_AVERAGE);
    assertThat(routeOptions.getMillis()).isEqualTo(arriveByInMillis);
    assertThat(routeOptions.getTransferTime()).isEqualTo(expectedTransferTime);
    assertThat(routeOptions.getMaxWalkingTime()).isEqualTo(expectedMaxWalkingTime);
  }

  @Test
  public void shouldBuildRouteOptionsProperly_2() {
    final long arriveByInMillis = System.currentTimeMillis();
    final RouteOptions routeOptions = new RouteOptions
        .Builder(new Location(), new Location())
        .walkingSpeed(WALKING_SPEED_AVERAGE)
        .cyclingSpeed(CYCLING_SPEED_FAST)
        .arriveBy(arriveByInMillis)
        .build();
    assertThat(routeOptions.getWalkingSpeed()).isEqualTo(WALKING_SPEED_AVERAGE);
    assertThat(routeOptions.getCyclingSpeed()).isEqualTo(CYCLING_SPEED_FAST);
    assertThat(routeOptions.getMillis()).isEqualTo(arriveByInMillis);
  }

  @Test
  public void shouldConvertToQueryProperly() {
    final long arriveByInMillis = System.currentTimeMillis();
    final int expectedTransferTime = 5;
    final Query query = new RouteOptions
        .Builder(new Location(), new Location())
        .walkingSpeed(WALKING_SPEED_AVERAGE)
        .cyclingSpeed(CYCLING_SPEED_FAST)
        .arriveBy(arriveByInMillis)
        .transferTime(expectedTransferTime)
        .build()
        .toQuery();
    assertThat(query.getArriveBy() / 1000).isEqualTo(arriveByInMillis / 1000);
    assertThat(query.getDepartAfter()).isEqualTo(-1L);
    assertThat(query.getWalkingSpeed()).isEqualTo(WALKING_SPEED_AVERAGE);
    assertThat(query.getCyclingSpeed()).isEqualTo(CYCLING_SPEED_FAST);
    assertThat(query.getTransferTime()).isEqualTo(expectedTransferTime);
  }
}