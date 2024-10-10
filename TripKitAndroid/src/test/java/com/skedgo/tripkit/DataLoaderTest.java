package com.skedgo.tripkit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observers.TestObserver;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class DataLoaderTest {
    private TestLoader loader;

    @Before
    public void setUp() {
        loader = new TestLoader();
    }

    @Test
    public void shouldLoadDataFromDiskIfMemoryCacheIsNotPresent() {
        final TestObserver<String> subscriber = loader.call().test();

        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();
        subscriber.assertTerminated();
        final List<String> events = subscriber.values();
        assertThat(events).hasSize(1).containsExactly("Awesome!");
    }

    @Test
    public void shouldUseMemoryCacheIfPresent() {
        final TestObserver<String> subscriber1 = loader.call().test();

        subscriber1.awaitTerminalEvent();
        subscriber1.assertNoErrors();
        subscriber1.assertTerminated();

        final TestObserver<String> subscriber2 = loader.call().test();

        subscriber2.awaitTerminalEvent();
        subscriber2.assertNoErrors();
        subscriber2.assertTerminated();
        final List<String> events = subscriber2.values();
        assertThat(events).hasSize(1).containsExactly("Awesome!");
    }

    @Test
    public void shouldThrowNoSuchElementException() {
        final EmptyLoader loader = new EmptyLoader();

        final TestObserver<String> subscriber = loader.call().test();

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.errors())
            .hasSize(1)
            .hasOnlyElementsOfType(NoSuchElementException.class);
    }

    private static final class TestLoader extends DataLoader<String> {
        private final AtomicInteger counter = new AtomicInteger();

        @Override
        protected Observable<String> getDataAsync() {
            return Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    if (counter.incrementAndGet() > 1) {
                        emitter.onError(new IllegalStateException("Loading from disk twice!"));
                    } else {
                        emitter.onNext("Awesome!");
                        emitter.onComplete();
                    }
                }
            });
        }
    }

    private static final class EmptyLoader extends DataLoader<String> {
        @Override
        protected Observable<String> getDataAsync() {
            return Observable.empty();
        }
    }
}