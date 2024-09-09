package com.skedgo.tripkit.account.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Test

class SignUpTest {
    val userTokenRepository: UserTokenRepository = mock()
    val signUp: SignUp by lazy { SignUp(userTokenRepository) }

    @Test
    fun shouldSignUpWithCredentials() {
        val userToken = UserToken("Live long and prosper.")
        whenever(userTokenRepository.getUserTokenBySignUpCredentials(any()))
            .thenReturn(Observable.just(userToken))

        val credentials =
            SignUpCredentials("spock@vulcan.com", "Insufficient facts always invite danger.")
        val subscriber = signUp.execute(credentials).test()

        subscriber.assertValue(userToken)
        verify(userTokenRepository).getUserTokenBySignUpCredentials(eq(credentials))
    }
}
