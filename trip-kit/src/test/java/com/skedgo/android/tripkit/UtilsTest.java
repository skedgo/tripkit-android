package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TransportMode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class UtilsTest {
  @Test
  public void getCities_shouldReceiveCitiesFromRegion() {
    final Region region = new Region();
    final ArrayList<Region.City> expectedCities = new ArrayList<>(Arrays.asList(
        new Region.City(),
        new Region.City(),
        new Region.City()
    ));
    region.setCities(expectedCities);

    final TestSubscriber<Location> subscriber = new TestSubscriber<>();
    Observable.just(region)
        .compose(Utils.getCities())
        .subscribe(subscriber);
    subscriber.assertNoErrors();
    final List<Location> events = subscriber.getOnNextEvents();
    assertThat(events).isEqualTo(expectedCities);
  }

  @Test
  public void getCities_shouldReceiveEmptyListForRegionHavingEmptyCityList() {
    final Region region = new Region();
    region.setCities(new ArrayList<Region.City>());

    final TestSubscriber<Location> subscriber = new TestSubscriber<>();
    Observable.just(region)
        .compose(Utils.getCities())
        .subscribe(subscriber);
    subscriber.assertNoErrors();
    subscriber.assertReceivedOnNext(Collections.<Location>emptyList());
  }

  @Test
  public void getCities_shouldReceiveEmptyListForRegionHavingNullCityList() {
    final Region region = new Region();
    final TestSubscriber<Location> subscriber = new TestSubscriber<>();
    Observable.just(region)
        .compose(Utils.getCities())
        .subscribe(subscriber);
    subscriber.assertNoErrors();
    subscriber.assertReceivedOnNext(Collections.<Location>emptyList());
  }

  @Test
  public void matchCityName_trueForNullKeyword() throws Exception {
    final Region.City city = new Region.City();
    assertThat(Utils.matchCityName(null).call(city)).isTrue();
  }

  @Test
  public void matchCityName_trueForEmptyKeyword() throws Exception {
    final Region.City city = new Region.City();
    assertThat(Utils.matchCityName("").call(city)).isTrue();
  }

  @Test
  public void matchCityName_trueForOnlySpaceKeyword() throws Exception {
    final Region.City city = new Region.City();
    assertThat(Utils.matchCityName("   ").call(city)).isTrue();
  }

  @Test
  public void matchCityName_trueIfContainingKeyword() throws Exception {
    final Region.City city = new Region.City();
    city.setName("Holy coOl!");
    assertThat(Utils.matchCityName("Cool").call(city)).isTrue();
  }

  @Test
  public void matchCityName_falseIfNotContainingKeyword() throws Exception {
    final Region.City city = new Region.City();
    city.setName("Holy cool!");
    assertThat(Utils.matchCityName("awesome").call(city)).isFalse();
  }

  @Test
  public void findModesByIds_shouldReturnModesCorrespondingToRequestedIds() {
    final HashMap<String, TransportMode> modeMap = new HashMap<>();
    for (String modeId : Arrays.asList("bus", "car", "motorbike", "taxi", "bicycle")) {
      final TransportMode mode = new TransportMode();
      mode.setId(modeId);
      modeMap.put(modeId, mode);
    }

    final List<String> modeIds = Arrays.asList("car", "motorbike", "bicycle");
    final List<TransportMode> result = Utils.findModesByIds(modeIds).call(modeMap);
    assertThat(result)
        .isNotNull()
        .extractingResultOf("getId")
        .hasSize(modeIds.size())
        .containsAll(modeIds);
  }

  /**
   * If mode map doesn't have modes requested by ids, just return whatever we can find.
   */
  @Test
  public void findModesByIds_shouldIgnoreModesThatAreNotFound() {
    // "motorbike" isn't found in this map.
    final HashMap<String, TransportMode> modeMap = new HashMap<>();
    for (String modeId : Arrays.asList("bus", "car", "walk", "taxi", "bicycle")) {
      final TransportMode mode = new TransportMode();
      mode.setId(modeId);
      modeMap.put(modeId, mode);
    }

    final List<String> modeIds = Arrays.asList("car", "motorbike", "bicycle");
    final List<TransportMode> result = Utils.findModesByIds(modeIds).call(modeMap);
    assertThat(result)
        .isNotNull()
        .extractingResultOf("getId")
        .hasSize(2)
        .containsAll(Arrays.asList("car", "bicycle"));
  }

  @Test
  public void findModesByIds_shouldReturnEmptyListIfAllModesCannotBeFound() {
    final HashMap<String, TransportMode> modeMap = new HashMap<>();
    for (String modeId : Arrays.asList("bus", "tram", "walk", "taxi", "ferry")) {
      final TransportMode mode = new TransportMode();
      mode.setId(modeId);
      modeMap.put(modeId, mode);
    }

    final List<String> modeIds = Arrays.asList("car", "motorbike", "bicycle");
    final List<TransportMode> result = Utils.findModesByIds(modeIds).call(modeMap);
    assertThat(result).isEmpty();
  }

  @Test
  public void toModeMap_shouldProduceEntryHavingKeyAsModeId() {
    final TransportMode bus = TransportMode.fromId("bus");
    final TransportMode walk = TransportMode.fromId("walk");
    final TransportMode car = TransportMode.fromId("car");
    final Map<String, TransportMode> modeMap = Utils.toModeMap().call(Arrays.asList(bus, walk, car));
    assertThat(modeMap)
        .hasSize(3)
        .containsEntry("bus", bus)
        .containsEntry("walk", walk)
        .containsEntry("car", car);
  }

  @Test
  public void toModeMap_shouldProduceEmptyMap() {
    final Map<String, TransportMode> modeMap = Utils.toModeMap().call(null);
    assertThat(modeMap).isEmpty();
  }
}