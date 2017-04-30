package skedgo.tripkit.datetime

import org.joda.time.DateTime
import rx.Observable

interface PrintFullDate {
  fun execute(dateTime: DateTime): Observable<String>
}