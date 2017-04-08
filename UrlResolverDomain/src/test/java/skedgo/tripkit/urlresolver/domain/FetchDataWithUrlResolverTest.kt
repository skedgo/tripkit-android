package skedgo.tripkit.urlresolver.domain

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import skedgo.tripkit.urlresolver.domain.FetchDataWithUrlResolver
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

class FetchDataWithUrlResolverTest {

  private val fetchDataWithUrlResolver: FetchDataWithUrlResolver<Any> by lazy {
    FetchDataWithUrlResolver<Any>()
  }

  @Test fun shouldFetchWithBaseUrl() {

    val response: Any = mock()

    val fetcher: (String) -> Observable<Any> = mock()
    whenever(fetcher(fetchDataWithUrlResolver.baseUrl)).thenReturn(Observable.just(response))

    val subscriber = TestSubscriber<Any>()
    fetchDataWithUrlResolver.execute(fetcher).subscribe(subscriber)

    subscriber.assertValue(response)

  }
}