package skedgo.tripkit.account.data

import android.content.Context
import android.content.SharedPreferences
import org.amshove.kluent.`should be equal to`
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class UserKeyRepositoryImplTest {
  private val prefs: SharedPreferences by lazy {
    RuntimeEnvironment.application.getSharedPreferences("", Context.MODE_PRIVATE)
  }
  private val userKeyRepository: UserKeyRepositoryImpl by lazy {
    UserKeyRepositoryImpl(prefs)
  }

  @Test
  fun `should return saved key`() {
    // Arrange.
    prefs.edit()
        .putString("userUUID", "key")
        .apply()

    // Act.
    val userKey = userKeyRepository.getUserKey()
        .test()
        .onNextEvents[0]

    // Assert.
    userKey `should be equal to` "key"
  }

  @Test
  fun `should return new key`() {

    // Act.
    val actualUserIdentifier = userKeyRepository.getUserKey()
        .test()
        .onNextEvents[0]

    // Assert.
    actualUserIdentifier.let { UUID.fromString(it) }
  }
}
