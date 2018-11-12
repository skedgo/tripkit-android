package skedgo.tripkit.a2brouting

import android.graphics.Color
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.common.model.Street
import com.skedgo.android.common.model.TransportMode
import org.amshove.kluent.`should equal`
import org.amshove.kluent.mock
import org.junit.Test

import org.junit.Assert.*

class GetTripLineTest {

  val getTripLine = GetTripLine()

  @Test
  fun `should return green for safe street with bicycle or wheelchair`() {
    val street = mock<Street>().apply {
      whenever(this.safe()).thenReturn(true)
    }
    getTripLine.getColorForWheelchairAndBicycle(street = street, modeId = TransportMode.ID_BICYCLE) `should equal` Color.GREEN
    getTripLine.getColorForWheelchairAndBicycle(street = street, modeId = TransportMode.ID_WHEEL_CHAIR) `should equal` Color.GREEN
  }

  @Test
  fun `should return null modes other than bicycle and wheelchair`() {
    getTripLine.getColorForWheelchairAndBicycle(mock(), modeId = TransportMode.ID_CAR) `should equal` null
  }

  @Test
  fun `should return red for dismount street with bicycle`() {
    val street = mock<Street>().apply {
      whenever(this.dismount()).thenReturn(true)
    }
    getTripLine.getColorForWheelchairAndBicycle(street = street, modeId = TransportMode.ID_BICYCLE) `should equal` Color.RED
  }

  @Test
  fun `should return yellow for unsafe street with bicycle or wheelchair`() {
    val street = mock<Street>().apply {
      whenever(this.safe()).thenReturn(false)
      whenever(this.dismount()).thenReturn(false)
    }
    getTripLine.getColorForWheelchairAndBicycle(street = street, modeId = TransportMode.ID_WHEEL_CHAIR) `should equal` Color.YELLOW
    getTripLine.getColorForWheelchairAndBicycle(street = street, modeId = TransportMode.ID_BICYCLE) `should equal` Color.YELLOW
  }
}