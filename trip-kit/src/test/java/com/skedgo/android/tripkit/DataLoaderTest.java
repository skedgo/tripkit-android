package com.skedgo.android.tripkit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DataLoaderTest {
  private TestLoader loader;

  @Before public void setUp() {
    loader = new TestLoader();
  }

  @Test public void shouldLoadDataFromDiskIfMemoryCacheIsNotPresent() {
    final TestSubscriber<String> subscriber = new TestSubscriber<>();
    loader.call().subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertTerminalEvent();
    final List<String> events = subscriber.getOnNextEvents();
    assertThat(events).hasSize(1).containsExactly("Awesome!");
  }

  @Test public void shouldUseMemoryCacheIfPresent() {
    final TestSubscriber<String> subscriber1 = new TestSubscriber<>();
    loader.call().subscribe(subscriber1);

    subscriber1.awaitTerminalEvent();
    subscriber1.assertNoErrors();
    subscriber1.assertTerminalEvent();

    final TestSubscriber<String> subscriber2 = new TestSubscriber<>();
    loader.call().subscribe(subscriber2);

    subscriber2.awaitTerminalEvent();
    subscriber2.assertNoErrors();
    subscriber2.assertTerminalEvent();
    final List<String> events = subscriber2.getOnNextEvents();
    assertThat(events).hasSize(1).containsExactly("Awesome!");
  }

  @Test public void shouldThrowNoSuchElementException() {
    final TestSubscriber<String> subscriber = new TestSubscriber<>();
    final EmptyLoader loader = new EmptyLoader();
    loader.call().subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    assertThat(subscriber.getOnErrorEvents())
        .hasSize(1)
        .hasOnlyElementsOfType(NoSuchElementException.class);
  }

  private static final class TestLoader extends DataLoader<String> {
    private final AtomicInteger counter = new AtomicInteger();

    @Override protected Observable<String> getDataAsync() {
      return Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
          if (counter.incrementAndGet() > 1) {
            subscriber.onError(new IllegalStateException("Loading from disk twice!"));
          } else {
            subscriber.onNext("Awesome!");
            subscriber.onCompleted();
          }
        }
      });
    }
  }

  private static final class EmptyLoader extends DataLoader<String> {
    @Override protected Observable<String> getDataAsync() {
      return Observable.empty();
    }
  }
}