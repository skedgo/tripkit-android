package com.skedgo.tripkit.datetime

import io.reactivex.Flowable
import org.joda.time.DateTime

interface PrintFullDate {
    fun execute(dateTime: DateTime): Flowable<String>
}