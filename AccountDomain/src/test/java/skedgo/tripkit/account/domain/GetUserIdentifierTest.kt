package skedgo.tripkit.account.domain

import com.nhaarman.mockito_kotlin.mock
import static org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito.given
import rx.Observable.just

class GetUserIdentifierTest {
  val userRepository: UserRepository = mock()
  val getUserIdentifier: GetUserIdentifier by lazy {
    GetUserIdentifier(userRepository)
  }

  @Test fun userIdentifierShouldBeHashCodeConcat() {
    val user = User("spock@vulcan.com", "com.vulcan")
    given(userRepository.getUser()).willReturn(just(user))
    val expectedUserIdentifier = "${user.type.hashCode()}${user.name.hashCode()}"

    val actualUserIdentifier = getUserIdentifier.execute().toBlocking().first()
    assertThat(actualUserIdentifier).isEqualTo(expectedUserIdentifier)
  }
}
