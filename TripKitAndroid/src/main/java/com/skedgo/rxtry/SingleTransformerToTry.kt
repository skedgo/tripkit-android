package com.skedgo.rxtry

import io.reactivex.Single
import io.reactivex.SingleTransformer

internal class SingleTransformerToTry<T>(
    private val predicate: (Throwable) -> Boolean
) : SingleTransformer<T, Try<T>> {
    override fun apply(upstream: Single<T>): Single<Try<T>> {
        return upstream
            .map<Try<T>> { Success(it) }
            .onErrorResumeNext {
                when (predicate(it)) {
                    true -> Single.just(Failure(it))
                    false -> Single.error(it)
                }
            }
    }
}