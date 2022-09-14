package com.skedgo.tripkit.account.domain

import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import io.reactivex.Observable.just

class LogOutTest {
  val userTokenRepository: UserTokenRepository = mock()
  val logOut: LogOut by lazy { LogOut(userTokenRepository) }

  @Test fun shouldClearTokenByLoggingOut() {
    given(userTokenRepository.clearUserTokenByLoggingOut()).willReturn(just(true))

    val subscriber = logOut.execute().test()

    subscriber.assertValue(true)
    verify(userTokenRepository).clearUserTokenByLoggingOut()
  }
}
