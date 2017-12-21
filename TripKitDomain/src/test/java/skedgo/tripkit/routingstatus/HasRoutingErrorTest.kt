package skedgo.tripkit.routingstatus

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable

class HasRoutingErrorTest {
  private val getRoutingStatus: RoutingStatusRepository = mock()
  private val hasRoutingError: HasRoutingError by lazy {
    HasRoutingError(getRoutingStatus)
  }

  @Test
  fun `message should be empty`() {
    // Arrange.
    whenever(getRoutingStatus.getRoutingStatus(any())).thenReturn(Observable.just(
        RoutingStatus(
            routingRequestId = "123",
            status = Status.Error("some message")
        ))
    )

    // Act & assert.
    hasRoutingError.execute(Observable.just("123"))
        .test()
        .assertValue(Pair(true, ""))
  }
}
