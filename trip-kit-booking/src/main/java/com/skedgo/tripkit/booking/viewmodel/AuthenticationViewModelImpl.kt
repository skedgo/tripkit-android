package com.skedgo.tripkit.booking.viewmodel

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class AuthenticationViewModelImpl : AuthenticationViewModel {
    private val isSuccessful: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    private val url: BehaviorSubject<String> = BehaviorSubject.create()

    override fun url(): BehaviorSubject<String> {
        return url
    }

    override fun isSuccessful(): Observable<Boolean> {
        // Directly return the BehaviorSubject as an Observable
        return isSuccessful.hide()
    }

    override fun verify(param: Any): Observable<Boolean> {
        return Observable.create { emitter ->
            val urlString = param.toString()
            val isUrlValid = urlString.contains("skedgo.com")
            emitter.onNext(isUrlValid)
            emitter.onComplete()
        }.doOnNext { isSuccessful.onNext(it) }
    }
}