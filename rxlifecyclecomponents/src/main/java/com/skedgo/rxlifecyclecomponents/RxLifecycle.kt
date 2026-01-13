package com.skedgo.rxlifecyclecomponents

import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

fun <T, E : Any> Observable<T>.bindToLifecycle(provider: LifecycleProvider<E>): Observable<T>
        = this.compose<T>(provider.bindToLifecycle<T>())

fun <T, E : Any> Observable<T>.bindUntilEvent(provider: LifecycleProvider<E>, event: E): Observable<T>
        = this.compose<T>(provider.bindUntilEvent(event))

fun <E : Any> Completable.bindToLifecycle(provider: LifecycleProvider<E>): Completable
        = this.compose(provider.bindToLifecycle<Completable>())

fun <E : Any> Completable.bindUntilEvent(provider: LifecycleProvider<E>, event: E): Completable
        = this.compose(provider.bindUntilEvent<Completable>(event))

fun <T, E : Any> Single<T>.bindToLifecycle(provider: LifecycleProvider<E>): Single<T>
        = this.compose(provider.bindToLifecycle<T>())

fun <T, E : Any> Single<T>.bindUntilEvent(provider: LifecycleProvider<E>, event: E): Single<T>
        = this.compose(provider.bindUntilEvent<T>(event))