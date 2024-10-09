package com.skedgo.tripkit.booking.viewmodel

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.BookingService
import com.skedgo.tripkit.booking.FormField
import com.skedgo.tripkit.booking.FormFieldJsonAdapter
import com.skedgo.tripkit.booking.InputForm
import com.skedgo.tripkit.booking.LinkFormField
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.subjects.BehaviorSubject
import org.apache.commons.collections4.CollectionUtils

class BookingViewModelImpl(private val bookingService: BookingService) : BookingViewModel {

    // Replace Var with BehaviorSubject
    private val nextBookingForm: BehaviorSubject<Param> = BehaviorSubject.create()
    private val bookingForm: BehaviorSubject<BookingForm> = BehaviorSubject.create()
    private val isDone: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val mIsFetching: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    private var param: Param? = null

    companion object {
        fun createGson(): Gson {
            return GsonBuilder().registerTypeAdapter(
                FormField::class.java,
                FormFieldJsonAdapter()
            ).create()
        }
    }

    override fun bookingForm(): Flowable<BookingForm> {
        return bookingForm.hide().toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun nextBookingForm(): Flowable<Param> {
        return nextBookingForm.hide().toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun loadForm(param: Param?): Flowable<BookingForm>? {
        if (param == null) return null
        this.param = param
        return if (param.method == LinkFormField.METHOD_POST) {
            bookingService.postFormAsync(param.url.orEmpty(), param.postBody)
                .observeOn(mainThread())
                .doOnNext { form ->
                    form?.let { bookingForm.onNext(it) }
                }
                .doOnSubscribe {
                    mIsFetching.onNext(true)
                }
                .doOnComplete {
                    mIsFetching.onNext(false)
                }
        } else {
            bookingService.getFormAsync(param.url.orEmpty())
                .observeOn(mainThread())
                .doOnNext { form ->
                    form?.let { bookingForm.onNext(it) }
                }
                .doOnSubscribe {
                    mIsFetching.onNext(true)
                }
                .doOnComplete {
                    mIsFetching.onNext(false)
                }
        }
    }

    override fun isFetching(): Flowable<Boolean>? {
        return mIsFetching.hide().toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun paramFrom(form: BookingForm): Param {
        return Param.create(form)
    }

    override fun isDone(): Flowable<Boolean> {
        return isDone.hide().toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("CheckResult")
    override fun observeAuthentication(authenticationViewModel: AuthenticationViewModel) {
        authenticationViewModel.isSuccessful().subscribe { isSuccessful ->
            if (isSuccessful) {
                param?.let {
                    loadForm(it)?.subscribe(
                        { /* onSuccess */ },
                        { /* onError */ }
                    )
                }
            }
        }
    }

    override fun performAction(bookingForm: BookingForm): Flowable<Boolean> {
        val url = bookingForm.action?.url
        val postBody = InputForm.from(bookingForm.form)

        return if (url == null) {
            if (!canceled(bookingForm)) {
                isDone.onNext(true)
            } else {
                isDone.onNext(false)
            }
            Flowable.just(true)
        } else {
            bookingForm.action?.let { action ->
                nextBookingForm.onNext(Param.create(action, postBody))
            }
            Flowable.just(false)
        }
    }

    override fun performAction(linkFormField: LinkFormField): Flowable<Boolean> {
        nextBookingForm.onNext(Param.create(linkFormField))
        return Flowable.just(false)
    }

    private fun canceled(bookingForm: BookingForm): Boolean {
        val formGroups = bookingForm.form
        if (!formGroups.isNullOrEmpty()) {
            val bookingStatusField = formGroups[0].fields[0]
            if ("Cancelled" == bookingStatusField.value) {
                return true
            }
        }
        return false
    }
}