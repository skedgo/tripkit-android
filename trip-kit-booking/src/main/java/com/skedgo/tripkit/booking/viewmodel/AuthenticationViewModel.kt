package com.skedgo.tripkit.booking.viewmodel

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

interface AuthenticationViewModel {
    fun url(): BehaviorSubject<String>

    fun isSuccessful(): Observable<Boolean>

    fun verify(param: Any): Observable<Boolean>
}