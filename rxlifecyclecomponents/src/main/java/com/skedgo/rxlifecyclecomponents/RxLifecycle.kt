package com.skedgo.rxlifecyclecomponents

import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

fun <T, E> Observable<T>.bindToLifecycle(provider: LifecycleProvider<E>): Observable<T>
        = this.compose<T>(provider.bindToLifecycle<T>())

fun <T, E> Observable<T>.bindUntilEvent(provider: LifecycleProvider<E>, event: E): Observable<T>
        = this.compose<T>(provider.bindUntilEvent(event))

fun <E> Completable.bindToLifecycle(provider: LifecycleProvider<E>): Completable
        = this.compose(provider.bindToLifecycle<Completable>())

fun <E> Completable.bindUntilEvent(provider: LifecycleProvider<E>, event: E): Completable
        = this.compose(provider.bindUntilEvent<Completable>(event))

fun <T, E> Single<T>.bindToLifecycle(provider: LifecycleProvider<E>): Single<T>
        = this.compose(provider.bindToLifecycle<T>())

fun <T, E> Single<T>.bindUntilEvent(provider: LifecycleProvider<E>, event: E): Single<T>
        = this.compose(provider.bindUntilEvent<T>(event))