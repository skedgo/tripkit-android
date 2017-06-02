package skedgo.tripkit.a2brouting

import com.google.gson.Gson
import com.skedgo.android.common.model.ServiceStop
import com.skedgo.android.common.rx.Var
import com.skedgo.android.common.util.Gsons
import org.amshove.kluent.*
import org.junit.Test
import skedgo.tripkit.routing.RoutingResponse
import skedgo.tripkit.routing.TripSegment
import thuytrinh.mockwebserverrule.MockWebServerRule.createMockResponse

@Suppress("IllegalIdentifier")
class SetTripStopsFromSegmentTemplateTest : BaseUnitTest() {

  val gson: Gson = Gsons.createForLowercaseEnum()
  val setTripStopsFromSegmentTemplate by lazy { SetTripStopsFromSegmentTemplate(gson) }

  @Test fun `should set stops in segment`() {

    val mockResponse = createMockResponse("/stops-in-templates.json")
    val routingJson = mockResponse.body.readUtf8()

    val gson = Gsons.createForLowercaseEnum()
    val response = gson.fromJson<RoutingResponse>(routingJson, RoutingResponse::class.java)

    val segmentTemplateMap = response.createSegmentTemplateMap()
    val segment: TripSegment = mock()
    val stops: Var<List<ServiceStop>> = Var.create<List<ServiceStop>>()
    When calling segment.templateHashCode itReturns (1935529356)
    When calling segment.stops() itReturns (stops)

    setTripStopsFromSegmentTemplate.setStopsFromSegmentTemplate(segmentTemplateMap, segment, 100)

    segment.stops() `should not be` null

    val newStops = segment.stops().value()

    newStops.size `should be` 5
    newStops[0].code `should equal to` "2000429"
    newStops[0].arrivalTime `should equal to` (-126 + 100)

    newStops[1].code `should equal to` "200049"
    newStops[1].arrivalTime `should equal to` (-6 + 100)

    newStops[2].code `should equal to` "200055"
    newStops[2].arrivalTime `should equal to` (54 + 100)

    newStops[3].code `should equal to` "200057"
    newStops[3].arrivalTime `should equal to` (174 + 100)

    newStops[4].code `should equal to` "2000421"
    newStops[4].arrivalTime `should equal to` (0 + 100)

  }

  @Test fun `should set not stops in segment`() {

    val mockResponse = createMockResponse("/stops-in-templates.json")
    val routingJson = mockResponse.body.readUtf8()

    val gson = Gsons.createForLowercaseEnum()
    val response = gson.fromJson<RoutingResponse>(routingJson, RoutingResponse::class.java)

    val segmentTemplateMap = response.createSegmentTemplateMap()
    val segment: TripSegment = mock()
    val stops: Var<List<ServiceStop>> = Var.create<List<ServiceStop>>()
    When calling segment.templateHashCode itReturns (1921867951)
    When calling segment.stops() itReturns (stops)

    setTripStopsFromSegmentTemplate.setStopsFromSegmentTemplate(segmentTemplateMap, segment, 100)

    segment.stops() `should not be` null

    segment.stops().value() `should be` null

  }
}