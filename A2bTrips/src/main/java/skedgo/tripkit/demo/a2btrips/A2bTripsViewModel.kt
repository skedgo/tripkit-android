package skedgo.tripkit.demo.a2btrips

import android.view.View
import rx.Observable
import rx.subjects.BehaviorSubject

class A2bTripsViewModel {
  private val _progressVisibility: BehaviorSubject<Int> = BehaviorSubject.create(View.GONE)
  val progressVisibility: Observable<Int> get() = _progressVisibility.asObservable()
}
