package skedgo.iseventincluded.data

import android.content.Context
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import rx.observers.TestSubscriber
import skedgo.iseventincluded.domain.IsEventIncluded

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class IsEventIncludedRepositoryImplTest {
  val prefs = RuntimeEnvironment.application.getSharedPreferences("", Context.MODE_PRIVATE)
  internal val repository: IsEventIncludedRepositoryImpl by lazy {
    IsEventIncludedRepositoryImpl(prefs)
  }

  @Test fun valueShouldBeNotSpecifiedYet() {
    val subscriber = TestSubscriber<IsEventIncluded>()
    repository.getIsEventIncluded("spock@vulcan.com")
        .subscribe(subscriber)

    subscriber.assertValue(IsEventIncluded.NOT_SPECIFIED_YET)
  }

  @Test fun valueShouldBeYes() {
    prefs.edit().putString("spock@vulcan.com", "YES").apply()

    val subscriber = TestSubscriber<IsEventIncluded>()
    repository.getIsEventIncluded("spock@vulcan.com")
        .subscribe(subscriber)

    subscriber.assertValue(IsEventIncluded.YES)
  }

  @Test fun valueShouldBeNo() {
    prefs.edit().putString("spock@vulcan.com", "NO").apply()

    val subscriber = TestSubscriber<IsEventIncluded>()
    repository.getIsEventIncluded("spock@vulcan.com")
        .subscribe(subscriber)

    subscriber.assertValue(IsEventIncluded.NO)
  }
}
