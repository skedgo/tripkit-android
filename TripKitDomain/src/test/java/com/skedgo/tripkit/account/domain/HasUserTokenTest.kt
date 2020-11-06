package com.skedgo.tripkit.account.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import io.reactivex.Observable.*
import io.reactivex.subjects.PublishSubject

class HasUserTokenTest {
  val userTokenRepository: UserTokenRepository = mock()
  val hasUserToken: HasUserToken by lazy { HasUserToken(userTokenRepository) }

  @Test fun shouldEmitTrueWhenThereIsUserToken() {
    val userToken = UserToken("Without followers, evil cannot spread.")
    whenever(userTokenRepository.getLastKnownUserToken()).thenReturn(just(userToken))
    whenever(userTokenRepository.onUserTokenChanged()).thenReturn(never())

    val subscriber = hasUserToken.execute().test()
    subscriber.assertValue(true)
  }

  @Test fun shouldEmitFalseWhenThereIsNoUserToken() {
    whenever(userTokenRepository.getLastKnownUserToken()).thenReturn(empty())
    whenever(userTokenRepository.onUserTokenChanged()).thenReturn(never())

    val subscriber = hasUserToken.execute().test()
    subscriber.assertValue(false)
  }

  @Test fun shouldReEmitWhenUserTokenHasChanged() {
    val userToken = UserToken("Without followers, evil cannot spread.")
    whenever(userTokenRepository.getLastKnownUserToken()).thenReturn(just(userToken))

    val onUserTokenChanged: PublishSubject<Any> = PublishSubject.create()
    whenever(userTokenRepository.onUserTokenChanged()).thenReturn(onUserTokenChanged)

    val subscriber = hasUserToken.execute().test()

    subscriber.assertValue(true)

    onUserTokenChanged.onNext(String())
    subscriber.assertValues(true, true)
  }
}
