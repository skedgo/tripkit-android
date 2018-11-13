package skedgo.tripkit.routing

import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should be true`
import org.junit.Assert.*
import org.junit.Test

class TripGroupExtensionsKtTest {

  @Test
  fun `should be child mode`() {
    "cy_bic-s_Melbourne_BikeShare".isChildModeOf("cy_bic-s").`should be true`()
    "pt_pub_train".isChildModeOf("pt_pub").`should be true`()
  }

  @Test
  fun `should not be child mode`() {
    "cy_bic-s_Melbourne_BikeShare".isChildModeOf("cy_bic").`should be false`()
  }
}