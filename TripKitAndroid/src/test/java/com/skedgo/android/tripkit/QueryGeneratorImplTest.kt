package com.skedgo.android.tripkit

import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.Query
import com.skedgo.android.common.model.Region
import com.skedgo.android.common.model.TransportMode
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import rx.Observable
import rx.functions.Func2
import rx.observers.TestSubscriber
import java.util.*
import java.util.Arrays.asList

@RunWith(TestRunner::class)
class QueryGeneratorImplTest {
  @Mock
  internal var regionService: RegionService? = null
  private var queryGenerator: Func2<Query, TransportModeFilter, Observable<List<Query>>>? = null

  internal var transportModeFilter = object : TransportModeFilter {}

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    queryGenerator = QueryGeneratorImpl(regionService!!)
  }

  @Test
  fun shouldPropagateNullPointerExceptionIfDepartureIsNull() {
    val subscriber = TestSubscriber<List<Query>>()
    queryGenerator!!.call(Query(), transportModeFilter).subscribe(subscriber)
    subscriber.awaitTerminalEvent()
    assertThat(subscriber.onErrorEvents)
        .hasSize(1)
        .hasOnlyElementsOfType(NullPointerException::class.java)
        .extractingResultOf("getMessage")
        .containsExactly("Departure is null")
  }

  @Test
  fun shouldPropagateNullPointerExceptionIfArrivalIsNull() {
    val query = Query()
    query.setFromLocation(Location())

    val subscriber = TestSubscriber<List<Query>>()
    queryGenerator!!.call(query, transportModeFilter).subscribe(subscriber)
    subscriber.awaitTerminalEvent()
    assertThat(subscriber.onErrorEvents)
        .hasSize(1)
        .hasOnlyElementsOfType(NullPointerException::class.java)
        .extractingResultOf("getMessage")
        .containsExactly("Arrival is null")
  }

  @Test
  fun shouldPropagateOutOfRegionsExceptionIfDepartureIsUnsupported() {
    val query = Query()
    val departure = Location(1.0, 2.0)
    query.setFromLocation(departure)
    val arrival = Location(3.0, 4.0)
    query.setToLocation(arrival)

    val error = OutOfRegionsException(null, departure.lat, departure.lon)
    `when`(regionService!!.getRegionByLocationAsync(ArgumentMatchers.any(Location::class.java)))
        .thenAnswer { invocation ->
          val argument = invocation.getArgument<Location>(0)
          if (argument === departure) {
            Observable.error(error)
          } else if (argument === arrival) {
            Observable.just(Region())
          } else {
            throw IllegalArgumentException()
          }
        }

    val subscriber = TestSubscriber<List<Query>>()
    queryGenerator!!.call(query, transportModeFilter).subscribe(subscriber)
    subscriber.awaitTerminalEvent()
    assertThat(subscriber.onErrorEvents)
        .hasSize(1)
        .hasOnlyElementsOfType(OutOfRegionsException::class.java)
        .containsExactly(error)
  }

  @Test
  fun shouldPropagateOutOfRegionsExceptionIfArrivalIsUnsupported() {
    val query = Query()
    val departure = Location(1.0, 2.0)
    query.setFromLocation(departure)
    val arrival = Location(3.0, 4.0)
    query.setToLocation(arrival)

    val error = OutOfRegionsException(null, arrival.lat, arrival.lon)
    `when`(regionService!!.getRegionByLocationAsync(ArgumentMatchers.any(Location::class.java)))
        .thenAnswer { invocation ->
          val argument = invocation.getArgument<Location>(0)
          if (argument === arrival) {
            Observable.error(error)
          } else if (argument === departure) {
            Observable.just(Region())
          } else {
            throw IllegalArgumentException()
          }
        }

    val subscriber = TestSubscriber<List<Query>>()
    queryGenerator!!.call(query, transportModeFilter).subscribe(subscriber)
    subscriber.awaitTerminalEvent()
    assertThat(subscriber.onErrorEvents)
        .hasSize(1)
        .hasOnlyElementsOfType(OutOfRegionsException::class.java)
        .containsExactly(error)
  }

  @Test
  fun shouldGenerateQueriesProperly() {
    val query = Query()
    val departure = Location(12.972, 77.596)
    query.setFromLocation(departure)
    query.setToLocation(Location(12.993684, 77.613473))

    val modeMap = createSampleModeMap()
    `when`(regionService!!.getTransportModesAsync())
        .thenReturn(Observable.just(modeMap))
    `when`(regionService!!.getRegionByLocationAsync(ArgumentMatchers.any(Location::class.java)))
        .thenReturn(Observable.just(createBangaloreRegion()))

    val subscriber = TestSubscriber<List<Query>>()
    queryGenerator!!.call(query, transportModeFilter).subscribe(subscriber)

    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()
    subscriber.assertTerminalEvent()
    val queries = subscriber.onNextEvents[0]
    assertThat(queries).isNotNull.hasSize(9).doesNotContainNull()
  }

  private fun createSampleModeMap(): Map<String, TransportMode> {
    val modeMap = HashMap<String, TransportMode>()
    modeMap["pt_pub"] = TransportMode()
    modeMap["ps_tax"] = TransportMode()
    modeMap["me_car"] = TransportMode()
    modeMap["me_car-s_CND"] = TransportMode()
    modeMap["me_car-s_GOG"] = TransportMode()
    modeMap["me_mot"] = TransportMode()
    modeMap["cy_bic"] = TransportMode()
    modeMap["wa_wal"] = TransportMode()

    val schoolBusMode = TransportMode()
    schoolBusMode.setImplies(ArrayList(listOf("pt_pub")))
    modeMap["pt_sch"] = schoolBusMode

    val shuttleMode = TransportMode()
    shuttleMode.setImplies(ArrayList(asList("ps_tax", "cy_bic-s_AUSTIN")))
    modeMap["ps_shu"] = shuttleMode
    return modeMap
  }

  private fun createBangaloreRegion(): Region {
    val region = Region()
    region.setEncodedPolyline("_}{kA_a~tM_g{C??_ibE~f{C?")
    region.setName("IN_Bangalore")
    region.setTransportModeIds(ArrayList(asList(
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
    )))
    region.setURLs(ArrayList(listOf("https://inflationary-in-bangalore.tripgo.skedgo.com/satapp")))
    region.timezone = "Asia/Calcutta"
    return region
  }
}