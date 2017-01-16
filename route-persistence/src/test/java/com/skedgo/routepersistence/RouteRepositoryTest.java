package com.skedgo.routepersistence;

import android.util.Pair;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class RouteRepositoryTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock RouteStore routeStore;
  @Mock Func0<Long> currentMillisProvider;
  private RouteRepository repo;

  @Before public void before() {
    repo = new RouteRepository(routeStore, currentMillisProvider);
  }

  @Test public void shouldInvokeStoreToDeletePastRoutes() {
    when(routeStore.deleteAsync(any(Pair.class)))
        .thenReturn(Observable.empty());
    when(currentMillisProvider.call()).thenReturn(5L);

    repo.deletePastRoutesAsync().subscribe();

    verify(routeStore).deleteAsync(any(Pair.class));
    verify(currentMillisProvider).call();
  }

  @Test public void countSubscribers() {
    final AtomicInteger subscribeCount = new AtomicInteger(0);
    final AtomicInteger unsubscribeCount = new AtomicInteger(0);
    final Observable<Integer> s = Observable
        .fromCallable(new Callable<Integer>() {
          @Override public Integer call() throws Exception {
            Thread.sleep(TimeUnit.SECONDS.toMillis(2));
            return 0;
          }
        })
        .cache()
        .doOnSubscribe(new Action0() {
          @Override public void call() {
            subscribeCount.incrementAndGet();
          }
        })
        .doOnUnsubscribe(new Action0() {
          @Override public void call() {
            unsubscribeCount.incrementAndGet();
          }
        });
    final TestSubscriber<Integer> s0 = new TestSubscriber<>();
    s.subscribe(s0);
    final TestSubscriber<Integer> s1 = new TestSubscriber<>();
    s.subscribe(s1);

    s0.awaitTerminalEvent();
    s1.awaitTerminalEvent();
    assertThat(subscribeCount.get()).isEqualTo(2);
    assertThat(unsubscribeCount.get()).isEqualTo(2);
  }
}
