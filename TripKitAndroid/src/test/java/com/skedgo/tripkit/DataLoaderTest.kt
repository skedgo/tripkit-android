package com.skedgo.tripkit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.booking.ui.base.MockKTest
import io.mockk.MockKAnnotations
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.observers.TestObserver
import org.amshove.kluent.internal.assertFailsWith
import org.assertj.core.api.Java6Assertions
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.atomic.AtomicInteger

@RunWith(AndroidJUnit4::class)
class DataLoaderTest: MockKTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var loader: TestLoader

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        initRx()
        loader = TestLoader()
    }

    @After
    fun teardown() {
        tearDownRx()
    }

    @Test
    fun `should load data from disk if memory cache is not present`() {
        val subscriber = loader.call().test()

        subscriber.awaitTerminalEvent()
        subscriber.assertNoErrors()
        subscriber.assertTerminated()
        val events = subscriber.values()
        assertEquals(listOf("Awesome!"), events)
    }

    @Test
    fun `should use memory cache if present`() {
        val subscriber1 = loader.call().test()

        subscriber1.awaitTerminalEvent()
        subscriber1.assertNoErrors()
        subscriber1.assertTerminated()

        val subscriber2 = loader.call().test()

        subscriber2.awaitTerminalEvent()
        subscriber2.assertNoErrors()
        subscriber2.assertTerminated()
        val events = subscriber2.values()
        assertEquals(listOf("Awesome!"), events)
    }

    @Test
    fun `should throw NoSuchElementException`() {
        val loader = EmptyLoader()

        val subscriber = loader.call().test()

        subscriber.awaitTerminalEvent()
        assertEquals(1, subscriber.errors().size)
        assertFailsWith<NoSuchElementException> { throw subscriber.errors().first() }
    }

    private class TestLoader : DataLoader<String>() {
        private val counter = AtomicInteger()

        init {
            // Optionally initialize memoryCache with a default value for testing
            memoryCache.set("Awesome!")
        }

        override fun getDataAsync(): Observable<String> {
            return Observable.create { emitter ->
                if (counter.incrementAndGet() > 1) {
                    emitter.onError(IllegalStateException("Loading from disk twice!"))
                } else {
                    emitter.onNext("Awesome!")
                    emitter.onComplete()
                }
            }
        }
    }


    private class EmptyLoader : DataLoader<String>() {
        override fun call(): Observable<String> {
            return Observable.error(NoSuchElementException("No data available"))
        }

        override fun getDataAsync(): Observable<String> {
            return Observable.error(NoSuchElementException("No data available"))
        }
    }
}