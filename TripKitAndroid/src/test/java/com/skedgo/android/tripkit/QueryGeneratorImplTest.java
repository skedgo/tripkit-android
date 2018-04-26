package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TransportMode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observers.TestSubscriber;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class QueryGeneratorImplTest {
  @Mock RegionService regionService;
  private Func2<Query, ModeFilter, Observable<List<Query>>> queryGenerator;

  ModeFilter modeFilter = new DefaultModeFilter();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    queryGenerator = new QueryGeneratorImpl(regionService);
  }

  @Test
  public void shouldPropagateNullPointerExceptionIfDepartureIsNull() {
    final TestSubscriber<List<Query>> subscriber = new TestSubscriber<>();
    queryGenerator.call(new Query(), modeFilter).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    assertThat(subscriber.getOnErrorEvents())
        .hasSize(1)
        .hasOnlyElementsOfType(NullPointerException.class)
        .extractingResultOf("getMessage")
        .containsExactly("Departure is null");
  }

  @Test
  public void shouldPropagateNullPointerExceptionIfArrivalIsNull() {
    final Query query = new Query();
    query.setFromLocation(new Location());

    final TestSubscriber<List<Query>> subscriber = new TestSubscriber<>();
    queryGenerator.call(query, modeFilter).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    assertThat(subscriber.getOnErrorEvents())
        .hasSize(1)
        .hasOnlyElementsOfType(NullPointerException.class)
        .extractingResultOf("getMessage")
        .containsExactly("Arrival is null");
  }

  @Test
  public void shouldPropagateOutOfRegionsExceptionIfDepartureIsUnsupported() {
    final Query query = new Query();
    final Location departure = new Location(1, 2);
    query.setFromLocation(departure);
    final Location arrival = new Location(3, 4);
    query.setToLocation(arrival);

    final OutOfRegionsException error = new OutOfRegionsException(null, departure.getLat(), departure.getLon());
    when(regionService.getRegionByLocationAsync(any(Location.class)))
        .thenAnswer(new Answer<Observable<Region>>() {
          @Override
          public Observable<Region> answer(InvocationOnMock invocation) throws Throwable {
            final Location argument = invocation.getArgument(0);
            if (argument == departure) {
              return Observable.error(error);
            } else if (argument == arrival) {
              return Observable.just(new Region());
            } else {
              throw new IllegalArgumentException();
            }
          }
        });

    final TestSubscriber<List<Query>> subscriber = new TestSubscriber<>();
    queryGenerator.call(query, modeFilter).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    assertThat(subscriber.getOnErrorEvents())
        .hasSize(1)
        .hasOnlyElementsOfType(OutOfRegionsException.class)
        .containsExactly(error);
  }

  @Test
  public void shouldPropagateOutOfRegionsExceptionIfArrivalIsUnsupported() {
    final Query query = new Query();
    final Location departure = new Location(1, 2);
    query.setFromLocation(departure);
    final Location arrival = new Location(3, 4);
    query.setToLocation(arrival);

    final OutOfRegionsException error = new OutOfRegionsException(null, arrival.getLat(), arrival.getLon());
    when(regionService.getRegionByLocationAsync(any(Location.class)))
        .thenAnswer(new Answer<Observable<Region>>() {
          @Override
          public Observable<Region> answer(InvocationOnMock invocation) throws Throwable {
            final Location argument = invocation.getArgument(0);
            if (argument == arrival) {
              return Observable.error(error);
            } else if (argument == departure) {
              return Observable.just(new Region());
            } else {
              throw new IllegalArgumentException();
            }
          }
        });

    final TestSubscriber<List<Query>> subscriber = new TestSubscriber<>();
    queryGenerator.call(query, modeFilter).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    assertThat(subscriber.getOnErrorEvents())
        .hasSize(1)
        .hasOnlyElementsOfType(OutOfRegionsException.class)
        .containsExactly(error);
  }

  @Test
  public void shouldGenerateQueriesProperly() {
    final Query query = new Query();
    final Location departure = new Location(12.972, 77.596);
    query.setFromLocation(departure);
    query.setToLocation(new Location(12.993684, 77.613473));

    final Map<String, TransportMode> modeMap = createSampleModeMap();
    when(regionService.getTransportModesAsync())
        .thenReturn(Observable.just(modeMap));
    when(regionService.getRegionByLocationAsync(any(Location.class)))
        .thenReturn(Observable.just(createBangaloreRegion()));

    final TestSubscriber<List<Query>> subscriber = new TestSubscriber<>();
    queryGenerator.call(query, modeFilter).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertTerminalEvent();
    final List<Query> queries = subscriber.getOnNextEvents().get(0);
    assertThat(queries).isNotNull().hasSize(9).doesNotContainNull();
  }

  @NonNull
  private Map<String, TransportMode> createSampleModeMap() {
    final Map<String, TransportMode> modeMap = new HashMap<>();
    modeMap.put("pt_pub", new TransportMode());
    modeMap.put("ps_tax", new TransportMode());
    modeMap.put("me_car", new TransportMode());
    modeMap.put("me_car-s_CND", new TransportMode());
    modeMap.put("me_car-s_GOG", new TransportMode());
    modeMap.put("me_mot", new TransportMode());
    modeMap.put("cy_bic", new TransportMode());
    modeMap.put("wa_wal", new TransportMode());

    final TransportMode schoolBusMode = new TransportMode();
    schoolBusMode.setImplies(new ArrayList<>(singletonList("pt_pub")));
    modeMap.put("pt_sch", schoolBusMode);

    final TransportMode shuttleMode = new TransportMode();
    shuttleMode.setImplies(new ArrayList<>(asList("ps_tax", "cy_bic-s_AUSTIN")));
    modeMap.put("ps_shu", shuttleMode);
    return modeMap;
  }

  private Region createBangaloreRegion() {
    final Region region = new Region();
    region.setEncodedPolyline("_}{kA_a~tM_g{C??_ibE~f{C?");
    region.setName("IN_Bangalore");
    region.setTransportModeIds(new ArrayList<>(asList(
        "pt_pub",
        "pt_sch",
        "ps_tax",
        "ps_shu",
        "me_car",
        "me_car-s_CND",
        "me_car-s_GOG",
        "me_mot",
        "cy_bic",
        "wa_wal"
    )));
    region.setURLs(new ArrayList<>(singletonList("https://inflationary-in-bangalore.tripgo.skedgo.com/satapp")));
    region.setTimezone("Asia/Calcutta");
    return region;
  }
}