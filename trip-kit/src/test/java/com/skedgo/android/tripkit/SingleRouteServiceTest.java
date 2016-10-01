package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.TripGroup;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.annotation.Config;

import java.util.List;

import rx.subjects.PublishSubject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class SingleRouteServiceTest {
  @Rule public MockitoRule rule = MockitoJUnit.rule();
  @Mock RouteService routeService;
  private SingleRouteService singleRouteService;

  @Before public void before() {
    singleRouteService = new SingleRouteService(routeService);
  }

  /**
   * Given we've spawned a routing request (A) and
   * later spawned another routing request (B),
   * we expect that A should be cancelled before B is spawned.
   */
  @Test public void shouldCancelPreviousRequest_withQuery() {
    final PublishSubject<List<TripGroup>> emitter1 = PublishSubject.create();
    final PublishSubject<List<TripGroup>> emitter2 = PublishSubject.create();
    when(routeService.routeAsync(any(Query.class)))
        .thenReturn(emitter1.asObservable())
        .thenReturn(emitter2.asObservable());

    singleRouteService.routeAsync(mock(Query.class)).subscribe();
    assertThat(emitter1.hasObservers()).isTrue();

    singleRouteService.routeAsync(mock(Query.class)).subscribe();
    assertThat(emitter1.hasObservers()).isFalse();
    assertThat(emitter2.hasObservers()).isTrue();
  }

  /**
   * Given we've spawned a routing request (A) and
   * later spawned another routing request (B),
   * we expect that A should be cancelled before B is spawned.
   */
  @Test public void shouldCancelPreviousRequest_withRouteOptions() {
    final PublishSubject<TripGroup> emitter1 = PublishSubject.create();
    final PublishSubject<TripGroup> emitter2 = PublishSubject.create();
    when(routeService.routeAsync(any(RouteOptions.class)))
        .thenReturn(emitter1.asObservable())
        .thenReturn(emitter2.asObservable());

    singleRouteService.routeAsync(mock(RouteOptions.class)).subscribe();
    assertThat(emitter1.hasObservers()).isTrue();

    singleRouteService.routeAsync(mock(RouteOptions.class)).subscribe();
    assertThat(emitter1.hasObservers()).isFalse();
    assertThat(emitter2.hasObservers()).isTrue();
  }
}