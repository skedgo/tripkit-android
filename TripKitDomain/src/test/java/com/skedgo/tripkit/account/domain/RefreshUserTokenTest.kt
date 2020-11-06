package com.skedgo.tripkit.account.domain

import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import io.reactivex.Observable.empty
import io.reactivex.Observable.just

class RefreshUserTokenTest {
  val userTokenRepository: UserTokenRepository = mock()
  val silentlyLogIn: SilentlyLogIn = mock()
  val refreshUserToken: RefreshUserToken by lazy {
    RefreshUserToken(userTokenRepository, silentlyLogIn)
  }

  @Test fun shouldClearUserTokenAndThenSilentlyLogInAgain() {
    given(userTokenRepository.clearUserToken()).willReturn(just(true))
    given(silentlyLogIn.execute()).willReturn(empty())

    refreshUserToken.execute().subscribe()
    verify(userTokenRepository).clearUserToken()
    verify(silentlyLogIn).execute()
  }
}
