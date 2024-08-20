package com.skedgo.tripkit.account.domain

import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Observable.empty
import io.reactivex.Observable.just
import org.junit.Test
import org.mockito.BDDMockito.given

class GetUserTokenHeaderValueTest {
    val userTokenRepository: UserTokenRepository = mock()
    val getUserTokenHeaderValue: GetUserTokenHeaderValue by lazy {
        GetUserTokenHeaderValue(userTokenRepository)
    }

    @Test
    fun emitNullStringIfThereIsNoUserToken() {
        given(userTokenRepository.getLastKnownUserToken()).willReturn(empty())

        val subscriber = getUserTokenHeaderValue.execute().test()
        subscriber.assertValue(String())
    }

    @Test
    fun emitUserTokenValue() {
        val userToken = UserToken("Without followers, evil cannot spread")
        given(userTokenRepository.getLastKnownUserToken()).willReturn(just(userToken))

        val subscriber = getUserTokenHeaderValue.execute().test()
        subscriber.assertValue("Without followers, evil cannot spread")
    }
}
