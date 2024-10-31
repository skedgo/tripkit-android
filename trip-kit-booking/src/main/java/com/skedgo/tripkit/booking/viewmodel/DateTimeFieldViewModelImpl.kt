package com.skedgo.tripkit.booking.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.skedgo.tripkit.booking.DateTimeFormField
import io.reactivex.subjects.BehaviorSubject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.SECONDS

class DateTimeFieldViewModelImpl(
    private val field: DateTimeFormField
) : DateTimeViewModel, Parcelable {

    private val self: BehaviorSubject<DateTimeFieldViewModelImpl> = BehaviorSubject.create()
    private var calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = SECONDS.toMillis(field.mValue ?: 0L)
    }

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(DateTimeFormField::class.java.classLoader)!!
    ) {
        calendar = parcel.readSerializable() as Calendar
    }

    fun getSelf(): BehaviorSubject<DateTimeFieldViewModelImpl> {
        return self
    }

    fun setTime(hour: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        field.setValue(MILLISECONDS.toSeconds(calendar.timeInMillis))
        self.onNext(this)
    }

    fun setDate(year: Int, month: Int, day: Int) {
        calendar.set(year, month, day)
        field.setValue(MILLISECONDS.toSeconds(calendar.timeInMillis))
        self.onNext(this)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(field, flags)
        dest.writeSerializable(calendar)
    }

    override fun getYear(): Int {
        return calendar.get(Calendar.YEAR)
    }

    override fun getMonth(): Int {
        return calendar.get(Calendar.MONTH)
    }

    override fun getDay(): Int {
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    override fun getHour(): Int {
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    override fun getMinute(): Int {
        return calendar.get(Calendar.MINUTE)
    }

    override fun getTitle(): String {
        return field.title.orEmpty()
    }

    override fun getDate(): String {
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.US)
        return dateFormat.format(Date(SECONDS.toMillis(field.mValue ?: 0)))
    }

    override fun getTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.US)
        return timeFormat.format(Date(SECONDS.toMillis(field.mValue ?: 0)))
    }

    companion object CREATOR : Parcelable.Creator<DateTimeFieldViewModelImpl> {
        override fun createFromParcel(parcel: Parcel): DateTimeFieldViewModelImpl {
            return DateTimeFieldViewModelImpl(parcel)
        }

        override fun newArray(size: Int): Array<DateTimeFieldViewModelImpl?> {
            return arrayOfNulls(size)
        }

        fun create(dateTimeFormField: DateTimeFormField): DateTimeFieldViewModelImpl {
            return DateTimeFieldViewModelImpl(dateTimeFormField)
        }
    }
}