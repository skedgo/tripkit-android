package com.skedgo.tripkit.account.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable.empty
import io.reactivex.Observable.just
import org.junit.Test

class SilentlyLogInTest {
    val userTokenRepository: UserTokenRepository = mock()
    val getUserIdentifier: GetUserIdentifier = mock()
    val silentlyLogIn: SilentlyLogIn by lazy {
        SilentlyLogIn(userTokenRepository, getUserIdentifier)
    }

    @Test
    fun shouldNotGetUserTokenIfThereIsUserToken() {
        val userToken =
            UserToken("In critical moments, men sometimes see exactly what they wish to see.")
        whenever(userTokenRepository.getLastKnownUserToken()).thenReturn(just(userToken))

        val subscriber = silentlyLogIn.execute().test()

        subscriber.assertValue(userToken)
        verify(userTokenRepository).getLastKnownUserToken()
        verifyZeroInteractions(getUserIdentifier)
        verifyNoMoreInteractions(userTokenRepository)
    }

    @Test
    fun shouldReGetUserTokenIfThereIsNoUserToken() {
        val userToken =
            UserToken("In critical moments, men sometimes see exactly what they wish to see.")
        whenever(userTokenRepository.getLastKnownUserToken()).thenReturn(empty())
        whenever(userTokenRepository.getUserTokenByUserIdentifier(any())).thenReturn(just(userToken))
        whenever(getUserIdentifier.execute()).thenReturn(just("spock@vulcan.com"))

        val subscriber = silentlyLogIn.execute().test()

        subscriber.assertValue(userToken)
        verify(userTokenRepository).getLastKnownUserToken()
        verify(userTokenRepository).getUserTokenByUserIdentifier("spock@vulcan.com")
        verify(getUserIdentifier).execute()
    }
}
