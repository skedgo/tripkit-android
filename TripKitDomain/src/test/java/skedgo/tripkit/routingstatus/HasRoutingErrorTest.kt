package skedgo.tripkit.routingstatus

import com.gojuno.koptional.Some
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable

class GetRoutingErrorTest {
  private val getRoutingStatus: RoutingStatusRepository = mock()
  private val getRoutingError: GetRoutingError by lazy {
    GetRoutingError(getRoutingStatus)
  }

  @Test
  fun `status should be error`() {
    // Arrange.
    val status = Status.Error("error")
    whenever(getRoutingStatus.getRoutingStatus(any())).thenReturn(Observable.just(
        RoutingStatus(
            routingRequestId = "123",
            status = status
        ))
    )

    // Act & assert.
    getRoutingError.execute(Observable.just("123"))
        .test()
        .assertValue(Some(status))
  }
}
