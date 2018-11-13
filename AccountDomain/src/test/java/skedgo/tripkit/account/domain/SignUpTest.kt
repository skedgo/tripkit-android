package skedgo.tripkit.account.domain

import com.nhaarman.mockitokotlin2.*
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

class SignUpTest {
  val userTokenRepository: UserTokenRepository = mock()
  val signUp: SignUp by lazy { SignUp(userTokenRepository) }

  @Test fun shouldSignUpWithCredentials() {
    val userToken = UserToken("Live long and prosper.")
    whenever(userTokenRepository.getUserTokenBySignUpCredentials(any()))
        .thenReturn(Observable.just(userToken))

    val credentials = SignUpCredentials("spock@vulcan.com", "Insufficient facts always invite danger.")
    val subscriber = TestSubscriber<UserToken>()
    signUp.execute(credentials).subscribe(subscriber)

    subscriber.assertValue(userToken)
    verify(userTokenRepository).getUserTokenBySignUpCredentials(eq(credentials))
  }
}
