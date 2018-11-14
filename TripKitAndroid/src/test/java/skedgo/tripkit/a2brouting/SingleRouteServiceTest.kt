package skedgo.tripkit.a2brouting

import com.skedgo.android.common.model.Query
import com.skedgo.android.tripkit.TransportModeFilter
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import rx.Observable
import rx.subjects.PublishSubject
import skedgo.tripkit.routing.TripGroup

class SingleRouteServiceTest {
  @Rule
  var rule = MockitoJUnit.rule()
  @Mock
  internal var routeService: RouteService? = null

  internal var transportModeFilter = object : TransportModeFilter {}
  private var singleRouteService: SingleRouteService? = null

  @Before
  fun before() {
    singleRouteService = SingleRouteService(routeService!!)
  }

  /**
   * Given we've spawned a routing request (A) and
   * later spawned another routing request (B),
   * we expect that A should be cancelled before B is spawned.
   */
  @Test
  fun shouldCancelPreviousRequest_withQuery() {
    val emitter1 = PublishSubject.create<List<TripGroup>>()
    val emitter2 = PublishSubject.create<List<TripGroup>>()
    `when`<Observable<List<TripGroup>>>(routeService!!.routeAsync(ArgumentMatchers.any(Query::class.java), ArgumentMatchers.eq<TransportModeFilter>(transportModeFilter)))
        .thenReturn(emitter1.asObservable())
        .thenReturn(emitter2.asObservable())

    singleRouteService!!.routeAsync(mock(Query::class.java), transportModeFilter).subscribe()
    assertThat(emitter1.hasObservers()).isTrue()

    singleRouteService!!.routeAsync(mock(Query::class.java), transportModeFilter).subscribe()
    assertThat(emitter1.hasObservers()).isFalse()
    assertThat(emitter2.hasObservers()).isTrue()
  }
}
