package skedgo.tripkit.account.domain

import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import org.mockito.BDDMockito.given
import rx.Observable.empty
import rx.Observable.just
import rx.observers.TestSubscriber

class GetUserTokenHeaderValueTest {
  val userTokenRepository: UserTokenRepository = mock()
  val getUserTokenHeaderValue: GetUserTokenHeaderValue by lazy {
    GetUserTokenHeaderValue(userTokenRepository)
  }

  @Test fun emitNullStringIfThereIsNoUserToken() {
    given(userTokenRepository.getLastKnownUserToken()).willReturn(empty())

    val subscriber = TestSubscriber<String?>()
    getUserTokenHeaderValue.execute().subscribe(subscriber)
    subscriber.assertValue(null)
  }

  @Test fun emitUserTokenValue() {
    val userToken = UserToken("Without followers, evil cannot spread")
    given(userTokenRepository.getLastKnownUserToken()).willReturn(just(userToken))

    val subscriber = TestSubscriber<String?>()
    getUserTokenHeaderValue.execute().subscribe(subscriber)
    subscriber.assertValue("Without followers, evil cannot spread")
  }
}
