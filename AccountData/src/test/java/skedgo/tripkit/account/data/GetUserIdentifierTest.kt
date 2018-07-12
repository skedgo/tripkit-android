package skedgo.tripkit.account.data

import android.content.Context
import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.amshove.kluent.When
import org.amshove.kluent.`it returns`
import org.amshove.kluent.`should equal to`
import org.amshove.kluent.calling
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import rx.Completable
import rx.Single
import skedgo.tripkit.account.domain.GenerateUserKey

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class UserKeyRepositoryImplTest {
  private val prefs: SharedPreferences by lazy {
    RuntimeEnvironment.application.getSharedPreferences("", Context.MODE_PRIVATE)
  }
  private val generateUserKey: GenerateUserKey = mock()
  private val userKeyRepository: UserKeyRepositoryImpl by lazy {
    UserKeyRepositoryImpl(prefs, generateUserKey)
  }

  @Test
  fun `should return saved key`() {
    // Arrange.
    prefs.edit()
        .putString("userKey", "key")
        .apply()

    // Act.
    val userKey = userKeyRepository.getUserKey()
        .test()
        .onNextEvents[0]

    // Assert.
    userKey `should equal to` "key"
  }

  @Test
  fun `should return new key`() {
    // Arrange.
    When calling generateUserKey.execute() `it returns` Single.just("new key")

    // Act.
    val actualUserIdentifier = userKeyRepository.getUserKey()
        .test()
        .onNextEvents[0]

    // Assert.
    actualUserIdentifier `should equal to` "new key"
  }
}
