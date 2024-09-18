package com.skedgo.tripkit;

import com.skedgo.tripkit.common.model.Location;
import com.skedgo.tripkit.common.model.Region;
import com.skedgo.tripkit.common.model.TransportMode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
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

        final TestObserver<Location> subscriber = Observable.just(region)
            .compose(Utils.getCities())
            .test();
        subscriber.assertNoErrors();
        final List<Location> events = subscriber.values();
        assertThat(events).isEqualTo(expectedCities);
    }

    @Test
    public void getCities_shouldReceiveEmptyListForRegionHavingEmptyCityList() {
        final Region region = new Region();
        region.setCities(new ArrayList<Region.City>());

        final TestObserver<Location> subscriber = Observable.just(region)
            .compose(Utils.getCities())
            .test();
        subscriber.assertNoErrors();
        subscriber.assertValueSequence(Collections.<Location>emptyList());
    }

    @Test
    public void getCities_shouldReceiveEmptyListForRegionHavingNullCityList() {
        final Region region = new Region();
        final TestObserver<Location> subscriber = Observable.just(region)
            .compose(Utils.getCities())
            .test();
        subscriber.assertNoErrors();
        subscriber.assertValueSequence(Collections.<Location>emptyList());
    }

    @Test
    public void matchCityName_trueForNullKeyword() throws Exception {
        final Region.City city = new Region.City();
        assertThat(Utils.matchCityName(null).test(city)).isTrue();
    }

    @Test
    public void matchCityName_trueForEmptyKeyword() throws Exception {
        final Region.City city = new Region.City();
        assertThat(Utils.matchCityName("").test(city)).isTrue();
    }

    @Test
    public void matchCityName_trueForOnlySpaceKeyword() throws Exception {
        final Region.City city = new Region.City();
        assertThat(Utils.matchCityName("   ").test(city)).isTrue();
    }

    @Test
    public void matchCityName_trueIfContainingKeyword() throws Exception {
        final Region.City city = new Region.City();
        city.setName("Holy coOl!");
        assertThat(Utils.matchCityName("Cool").test(city)).isTrue();
    }

    @Test
    public void matchCityName_falseIfNotContainingKeyword() throws Exception {
        final Region.City city = new Region.City();
        city.setName("Holy cool!");
        assertThat(Utils.matchCityName("awesome").test(city)).isFalse();
    }

    @Test
    public void findModesByIds_shouldReturnModesCorrespondingToRequestedIds() throws Exception {
        final HashMap<String, TransportMode> modeMap = new HashMap<>();
        for (String modeId : Arrays.asList("bus", "car", "motorbike", "taxi", "bicycle")) {
            final TransportMode mode = new TransportMode();
            mode.setId(modeId);
            modeMap.put(modeId, mode);
        }

        final List<String> modeIds = Arrays.asList("car", "motorbike", "bicycle");
        final List<TransportMode> result = Utils.findModesByIds(modeIds).apply(modeMap);
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
    public void findModesByIds_shouldIgnoreModesThatAreNotFound() throws Exception {
        // "motorbike" isn't found in this map.
        final HashMap<String, TransportMode> modeMap = new HashMap<>();
        for (String modeId : Arrays.asList("bus", "car", "walk", "taxi", "bicycle")) {
            final TransportMode mode = new TransportMode();
            mode.setId(modeId);
            modeMap.put(modeId, mode);
        }

        final List<String> modeIds = Arrays.asList("car", "motorbike", "bicycle");
        final List<TransportMode> result = Utils.findModesByIds(modeIds).apply(modeMap);
        assertThat(result)
            .isNotNull()
            .extractingResultOf("getId")
            .hasSize(2)
            .containsAll(Arrays.asList("car", "bicycle"));
    }

    @Test
    public void findModesByIds_shouldReturnEmptyListIfAllModesCannotBeFound() throws Exception {
        final HashMap<String, TransportMode> modeMap = new HashMap<>();
        for (String modeId : Arrays.asList("bus", "tram", "walk", "taxi", "ferry")) {
            final TransportMode mode = new TransportMode();
            mode.setId(modeId);
            modeMap.put(modeId, mode);
        }

        final List<String> modeIds = Arrays.asList("car", "motorbike", "bicycle");
        final List<TransportMode> result = Utils.findModesByIds(modeIds).apply(modeMap);
        assertThat(result).isEmpty();
    }

    @Test
    public void toModeMap_shouldProduceEntryHavingKeyAsModeId() throws Exception {
        final TransportMode bus = TransportMode.fromId("bus");
        final TransportMode walk = TransportMode.fromId("walk");
        final TransportMode car = TransportMode.fromId("car");
        final Map<String, TransportMode> modeMap = Utils.toModeMap().apply(Arrays.asList(bus, walk, car));
        assertThat(modeMap)
            .hasSize(3)
            .containsEntry("bus", bus)
            .containsEntry("walk", walk)
            .containsEntry("car", car);
    }

    @Test
    public void toModeMap_shouldProduceEmptyMap() throws Exception {
        final Map<String, TransportMode> modeMap = Utils.toModeMap().apply(null);
        assertThat(modeMap).isEmpty();
    }
}