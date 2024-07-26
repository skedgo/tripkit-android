/**
 * Mainly for Java usage.
 */
@file:JvmName("ToTryTransformers")

package com.skedgo.rxtry

import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer

/**
 * Wraps any error from the upstream which matches [predicate] into [Failure] while
 * events emitted via [rx.Observer.onNext] will be wrapped into [Success].
 */
fun <T> toTryObservable(predicate: (Throwable) -> Boolean): ObservableTransformer<T, Try<T>> =
    ObservableTransformerToTry(predicate)

/**
 * Wraps any error from the upstream into [Failure] while
 * events emitted via [rx.Observer.onNext] will be wrapped into [Success].
 */
fun <T> toTryObservable(): ObservableTransformer<T, Try<T>> =
    ObservableTransformerToTry({ true })

/**
 * Wraps any error from the upstream which matches [predicate] into [Failure] while
 * events emitted via [rx.Observer.onNext] will be wrapped into [Success].
 */
fun <T> toTrySingle(predicate: (Throwable) -> Boolean): SingleTransformer<T, Try<T>> =
    SingleTransformerToTry(predicate)

/**
 * Wraps any error from the upstream into [Failure] while
 * events emitted via [rx.Observer.onNext] will be wrapped into [Success].
 */
fun <T> toTrySingle(): SingleTransformer<T, Try<T>> =
    SingleTransformerToTry({ true })