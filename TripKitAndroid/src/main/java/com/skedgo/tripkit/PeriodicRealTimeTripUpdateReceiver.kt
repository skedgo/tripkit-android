package com.skedgo.tripkit

import com.skedgo.TripKit
import com.skedgo.tripkit.routing.Trip
import com.skedgo.tripkit.routing.TripGroup
import io.reactivex.BackpressureStrategy.BUFFER
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style
import org.immutables.value.Value.Style.BuilderVisibility.PACKAGE
import org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

@Immutable
@Style(visibility = PRIVATE, builderVisibility = PACKAGE)
abstract class PeriodicRealTimeTripUpdateReceiver : RealTimeTripUpdateReceiver {
    private val stop = PublishSubject.create<Any>()

    override fun startAsync(): Flowable<Pair<Trip, TripGroup>> {
        return Flowable.interval(
            initialDelay().toLong(),
            period().toLong(),
            timeUnit(),
            Schedulers.trampoline()
        )
            .map { group().displayTrip!!.updateURL!! }
            .onBackpressureDrop()
            .compose { updateUrl ->
                val url = AtomicReference<String>()
                updateUrl
                    .flatMap { s: String ->
                        val lastUrl = url.get()
                        tripUpdater().getUpdateAsync(lastUrl ?: s)
                            .onErrorResumeNext(Observable.empty()).toFlowable(BUFFER)
                    }
                    .doOnNext { trip -> url.set(trip.updateURL) }
            }
            .map<Pair<Trip, TripGroup>>(Function<Trip, Pair<Trip, TripGroup>> { trip ->
                Pair(
                    trip,
                    group()
                )
            })
            .takeUntil(stop.toFlowable(BUFFER))
            .subscribeOn(Schedulers.io())
    }

    override fun stop() {
        stop.onNext(Any())
    }

    abstract fun tripUpdater(): TripUpdater

    abstract fun group(): TripGroup

    abstract fun initialDelay(): Int

    abstract fun period(): Int

    abstract fun timeUnit(): TimeUnit

    interface Builder {
        fun group(group: TripGroup?): Builder

        fun initialDelay(initialDelay: Int): Builder

        fun period(period: Int): Builder

        fun timeUnit(timeUnit: TimeUnit): Builder

        fun build(): RealTimeTripUpdateReceiver
    }

    companion object {
        fun builder(): Builder {
            return PeriodicRealTimeTripUpdateReceiverBuilder()
                .tripUpdater(TripKit.getInstance().tripUpdater)
        }
    }
}
