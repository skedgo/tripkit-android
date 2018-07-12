package skedgo.tripkit.account.domain

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.amshove.kluent.When
import org.amshove.kluent.`it returns`
import org.amshove.kluent.`should equal to`
import org.amshove.kluent.calling
import org.junit.Test
import rx.Completable
import rx.Single

class GetUserIdentifierTest {
  private val userRepository: UserRepository = mock()
  private val generateUserKey: GenerateUserKey = mock()
  private val getUserIdentifier: GetUserIdentifier by lazy {
    GetUserIdentifier(userRepository, generateUserKey)
  }

  @Test
  fun `should return saved key`() {
    // Arrange.
    When calling userRepository.getUserKey() `it returns` Single.just("key")

    // Act.
    val actualUserIdentifier = getUserIdentifier.execute()
        .test()
        .onNextEvents[0]

    // Assert.
    actualUserIdentifier `should equal to` "key"
  }

  @Test
  fun `should return new key`() {
    // Arrange.
    When calling userRepository.getUserKey() `it returns` Single.just("")
    When calling userRepository.setUserKey("new key") `it returns` Completable.complete()
    When calling generateUserKey.execute() `it returns` Single.just("new key")

    // Act.
    val actualUserIdentifier = getUserIdentifier.execute()
        .test()
        .onNextEvents[0]

    // Assert.
    actualUserIdentifier `should equal to` "new key"
    verify(userRepository).setUserKey("new key")
  }
}
