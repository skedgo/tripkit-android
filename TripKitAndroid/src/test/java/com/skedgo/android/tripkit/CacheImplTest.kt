package com.skedgo.android.tripkit

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import rx.Completable
import rx.Observable
import rx.Subscriber
import java.util.concurrent.atomic.AtomicInteger

class CacheImplTest : TripKitAndroidRobolectricTest() {
  @Test fun someInitialBehaviors() {
    val fetcherHitCounter = AtomicInteger()
    val fetcher = Completable.complete()
        .doOnCompleted { fetcherHitCounter.incrementAndGet() }
    val loader = Observable.create(object : Observable.OnSubscribe<String> {
      private val hitCounter = AtomicInteger(-1)

      override fun call(subscriber: Subscriber<in String>) {
        val hitCount = hitCounter.incrementAndGet()
        if (hitCount == 0) {
          // First time ever hit loader.
          subscriber.onNext(null)
        } else {
          subscriber.onNext(hitCount.toString())
        }

        subscriber.onCompleted()
      }
    })
    val cache = CacheImpl(fetcher, loader)
    assertThat(cache.async.toList().toBlocking().first())
        .describedAs("Should hit loader to retrieve data")
        .containsExactly("1")
    assertThat(fetcherHitCounter.get())
        .describedAs("Should hit fetcher to retrieve data")
        .isEqualTo(1)
    assertThat(cache.async.toList().toBlocking().first())
        .describedAs("Should give data cached in memory")
        .containsExactly("1")

    cache.invalidate()
    assertThat(cache.async.toList().toBlocking().first())
        .describedAs("After invalidation, should re-hit loader to retrieve data")
        .containsExactly("2")
    assertThat(fetcherHitCounter.get())
        .describedAs("After invalidation, shouldn't re-hit fetcher")
        .isEqualTo(1)
  }
}