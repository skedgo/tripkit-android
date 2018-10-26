package skedgo.tripkit.account.domain

import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import rx.Observable.just
import rx.observers.TestSubscriber

class LogOutTest {
  val userTokenRepository: UserTokenRepository = mock()
  val logOut: LogOut by lazy { LogOut(userTokenRepository) }

  @Test fun shouldClearTokenByLoggingOut() {
    given(userTokenRepository.clearUserTokenByLoggingOut()).willReturn(just(true))

    val subscriber = TestSubscriber<Boolean>()
    logOut.execute().subscribe(subscriber)

    subscriber.assertValue(true)
    verify(userTokenRepository).clearUserTokenByLoggingOut()
  }
}
