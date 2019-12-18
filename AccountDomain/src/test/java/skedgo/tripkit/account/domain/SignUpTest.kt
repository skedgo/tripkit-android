package skedgo.tripkit.account.domain

import com.nhaarman.mockitokotlin2.*
import org.junit.Test
import io.reactivex.Observable

class SignUpTest {
  val userTokenRepository: UserTokenRepository = mock()
  val signUp: SignUp by lazy { SignUp(userTokenRepository) }

  @Test fun shouldSignUpWithCredentials() {
    val userToken = UserToken("Live long and prosper.")
    whenever(userTokenRepository.getUserTokenBySignUpCredentials(any()))
        .thenReturn(Observable.just(userToken))

    val credentials = SignUpCredentials("spock@vulcan.com", "Insufficient facts always invite danger.")
    val subscriber = signUp.execute(credentials).test()

    subscriber.assertValue(userToken)
    verify(userTokenRepository).getUserTokenBySignUpCredentials(eq(credentials))
  }
}
