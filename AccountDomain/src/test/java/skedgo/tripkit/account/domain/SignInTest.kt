package skedgo.tripkit.account.domain

import com.nhaarman.mockitokotlin2.*
import org.junit.Test
import rx.Observable.just
import rx.observers.TestSubscriber

class SignInTest {
  val userTokenRepository: UserTokenRepository = mock()
  val signIn: SignIn by lazy { SignIn(userTokenRepository) }

  @Test fun shouldSignInWithCredentials() {
    val userToken = UserToken("Live long and prosper.")
    whenever(userTokenRepository.getUserTokenBySignInCredentials(any()))
        .thenReturn(just(userToken))

    val credentials = SignInCredentials("spock@vulcan.com", "Insufficient facts always invite danger.")
    val subscriber = TestSubscriber<UserToken>()
    signIn.execute(credentials).subscribe(subscriber)

    subscriber.assertValue(userToken)
    verify(userTokenRepository).getUserTokenBySignInCredentials(eq(credentials))
  }
}
