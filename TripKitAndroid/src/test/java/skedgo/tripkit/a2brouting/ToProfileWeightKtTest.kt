package skedgo.tripkit.a2brouting

import org.amshove.kluent.shouldEqualTo
import org.junit.Test

@Suppress("IllegalIdentifier")
class ToProfileWeightKtTest {
  @Test fun `should return 0 dot 1`() {
    0.toProfileWeight().shouldEqualTo(0.1f)
  }

  @Test fun `should return 2`() {
    100.toProfileWeight().shouldEqualTo(2f)
  }

  @Test(expected = IllegalArgumentException::class)
  fun `should throw error if value is smaller than 0`() {
    (-1).toProfileWeight()
  }

  @Test(expected = IllegalArgumentException::class)
  fun `should throw error if value is larger than 100`() {
    101.toProfileWeight()
  }
}
