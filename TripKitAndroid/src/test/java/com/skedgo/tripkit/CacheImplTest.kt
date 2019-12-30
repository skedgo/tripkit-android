package com.skedgo.tripkit

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class CacheImplTest : TripKitAndroidRobolectricTest() {
  @Test fun someInitialBehaviors() {
    val fetcherHitCounter = AtomicInteger(0)
    val fetcher = Completable.complete()
            .doOnComplete { fetcherHitCounter.incrementAndGet() }
    val loader = Observable.create(object : ObservableOnSubscribe<String> {
      private val hitCounter = AtomicInteger(-1)

      override fun subscribe(emitter: ObservableEmitter<String>) {
        val hitCount = hitCounter.incrementAndGet()
        if (hitCount > 0) emitter.onNext(hitCount.toString())
        emitter.onComplete()
      }
    })

    val cache = CacheImpl(fetcher, loader)
    assertThat(cache.async.toList().blockingGet())
        .describedAs("Should hit loader to retrieve data")
        .containsExactly("1")
    assertThat(fetcherHitCounter.get())
        .describedAs("Should hit fetcher to retrieve data")
        .isEqualTo(1)
    assertThat(cache.async.toList().blockingGet())
        .describedAs("Should give data cached in memory")
        .containsExactly("1")

    cache.invalidate()
    assertThat(cache.async.toList().blockingGet())
        .describedAs("After invalidation, should re-hit loader to retrieve data")
        .containsExactly("2")
    assertThat(fetcherHitCounter.get())
        .describedAs("After invalidation, shouldn't re-hit fetcher")
        .isEqualTo(1)
  }
}