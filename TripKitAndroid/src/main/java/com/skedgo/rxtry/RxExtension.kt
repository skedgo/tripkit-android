package com.skedgo.rxtry

import com.skedgo.tripkit.BuildConfig
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

fun <T> PublishSubject<T>.configureInterceptor(timeout: Long): Observable<out T> {
    return this.debounce(timeout, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T : Any> Observable<T>.subscribeWithErrorHandling(
    onError: (Throwable) -> Unit = { it.printThrowableStackTrace() },
    onNext: (T) -> Unit,
): Disposable = this.subscribe(onNext, onError)

fun Completable.subscribeWithErrorHandling(
    onError: (Throwable) -> Unit = { it.printThrowableStackTrace() },
    onComplete: () -> Unit = {},
): Disposable {
    return this.subscribe(onComplete, onError)
}

fun <T : Any> Single<T>.subscribeWithErrorHandling(
    onError: (Throwable) -> Unit = { it.printThrowableStackTrace() },
    onSuccess: (T) -> Unit,
): Disposable {
    return this.subscribe(onSuccess, onError)
}

fun <T : Any> Flowable<T>.subscribeWithErrorHandling(
    onError: (Throwable) -> Unit = { it.printThrowableStackTrace() },
    onSuccess: (T) -> Unit,
): Disposable {
    return this.subscribe(onSuccess, onError)
}

fun Throwable.printThrowableStackTrace() {
    if(BuildConfig.DEBUG) {
        this.printStackTrace()
    }
}