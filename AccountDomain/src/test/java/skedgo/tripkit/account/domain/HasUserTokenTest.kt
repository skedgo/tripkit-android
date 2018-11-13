package skedgo.tripkit.account.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import rx.Observable.*
import rx.observers.TestSubscriber
import rx.subjects.PublishSubject

class HasUserTokenTest {
  val userTokenRepository: UserTokenRepository = mock()
  val hasUserToken: HasUserToken by lazy { HasUserToken(userTokenRepository) }

  @Test fun shouldEmitTrueWhenThereIsUserToken() {
    val userToken = UserToken("Without followers, evil cannot spread.")
    whenever(userTokenRepository.getLastKnownUserToken()).thenReturn(just(userToken))
    whenever(userTokenRepository.onUserTokenChanged()).thenReturn(never())

    val subscriber = TestSubscriber<Boolean>()
    hasUserToken.execute().subscribe(subscriber)

    subscriber.assertValue(true)
  }

  @Test fun shouldEmitFalseWhenThereIsNoUserToken() {
    whenever(userTokenRepository.getLastKnownUserToken()).thenReturn(empty())
    whenever(userTokenRepository.onUserTokenChanged()).thenReturn(never())

    val subscriber = TestSubscriber<Boolean>()
    hasUserToken.execute().subscribe(subscriber)

    subscriber.assertValue(false)
  }

  @Test fun shouldReEmitWhenUserTokenHasChanged() {
    val userToken = UserToken("Without followers, evil cannot spread.")
    whenever(userTokenRepository.getLastKnownUserToken()).thenReturn(just(userToken))

    val onUserTokenChanged: PublishSubject<Any> = PublishSubject.create()
    whenever(userTokenRepository.onUserTokenChanged()).thenReturn(onUserTokenChanged)

    val subscriber = TestSubscriber<Boolean>()
    hasUserToken.execute().subscribe(subscriber)

    subscriber.assertValue(true)

    onUserTokenChanged.onNext(null)
    subscriber.assertValues(true, true)
  }
}
