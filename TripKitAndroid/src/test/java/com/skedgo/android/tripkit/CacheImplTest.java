package com.skedgo.android.tripkit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscriber;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class CacheImplTest {
  @Test public void someInitialBehaviors() {
    final AtomicInteger fetcherHitCounter = new AtomicInteger();
    final Observable<Void> fetcher = Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(Subscriber<? super Void> subscriber) {
        fetcherHitCounter.incrementAndGet();
        subscriber.onCompleted();
      }
    });
    final Observable<String> loader = Observable.create(new Observable.OnSubscribe<String>() {
      private final AtomicInteger hitCounter = new AtomicInteger(-1);

      @Override public void call(Subscriber<? super String> subscriber) {
        final int hitCount = hitCounter.incrementAndGet();
        if (hitCount == 0) {
          // First time ever hit loader.
          subscriber.onNext(null);
        } else {
          subscriber.onNext(String.valueOf(hitCount));
        }

        subscriber.onCompleted();
      }
    });
    final Cache<String> cache = new CacheImpl<>(fetcher, loader);
    assertThat(cache.getAsync().toList().toBlocking().first())
        .describedAs("Should hit loader to retrieve data")
        .containsExactly("1");
    assertThat(fetcherHitCounter.get())
        .describedAs("Should hit fetcher to retrieve data")
        .isEqualTo(1);
    assertThat(cache.getAsync().toList().toBlocking().first())
        .describedAs("Should give data cached in memory")
        .containsExactly("1");

    cache.invalidate();
    assertThat(cache.getAsync().toList().toBlocking().first())
        .describedAs("After invalidation, should re-hit loader to retrieve data")
        .containsExactly("2");
    assertThat(fetcherHitCounter.get())
        .describedAs("After invalidation, shouldn't re-hit fetcher")
        .isEqualTo(1);
  }
}