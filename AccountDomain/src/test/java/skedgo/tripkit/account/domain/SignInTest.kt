package skedgo.tripkit.account.domain

import com.nhaarman.mockitokotlin2.*
import org.junit.Test
import io.reactivex.Observable.just

class SignInTest {
  val userTokenRepository: UserTokenRepository = mock()
  val signIn: SignIn by lazy { SignIn(userTokenRepository) }

  @Test fun shouldSignInWithCredentials() {
    val userToken = UserToken("Live long and prosper.")
    whenever(userTokenRepository.getUserTokenBySignInCredentials(any()))
        .thenReturn(just(userToken))

    val credentials = SignInCredentials("spock@vulcan.com", "Insufficient facts always invite danger.")
    val subscriber = signIn.execute(credentials).test()

    subscriber.assertValue(userToken)
    verify(userTokenRepository).getUserTokenBySignInCredentials(eq(credentials))
  }
}
