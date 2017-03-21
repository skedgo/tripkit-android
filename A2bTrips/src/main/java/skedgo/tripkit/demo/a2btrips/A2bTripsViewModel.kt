package skedgo.tripkit.demo.a2btrips

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.view.View
import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.Query
import com.skedgo.android.common.model.TimeTag
import com.skedgo.android.tripkit.TripKit
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subjects.BehaviorSubject

class A2bTripsViewModel {
  private val _progressVisibility: BehaviorSubject<Int> = BehaviorSubject.create(View.GONE)
  val progressVisibility: Observable<Int> get() = _progressVisibility.asObservable()
  val trips: ObservableList<TripViewModel> = ObservableArrayList()

  fun showSampleTrips(): Observable<Unit>
      = Observable
      .fromCallable {
        val origin = Location(34.193984, -118.392930)
        origin.address = "11923 Vanowen St, North Hollywood, CA 91605, USA"

        val destination = Location(34.184923, -118.353576)
        destination.address = "2001-2027 N Maple St, Burbank, CA 91505, USA"

        val query = Query()
        query.fromLocation = origin
        query.toLocation = destination
        query.setTimeTag(TimeTag.createForLeaveNow())
        query
      }
      .flatMap { TripKit.singleton().routeService.routeAsync(it) }
      .doOnSubscribe { _progressVisibility.onNext(View.VISIBLE) }
      .doOnUnsubscribe { _progressVisibility.onNext(View.GONE) }
      .observeOn(mainThread())
      .doOnNext { trips.add(TripViewModel()) }
      .map { Unit }
}
