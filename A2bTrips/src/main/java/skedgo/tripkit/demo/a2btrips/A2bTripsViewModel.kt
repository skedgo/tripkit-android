package skedgo.tripkit.demo.a2btrips

import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.Query
import com.skedgo.android.common.model.TimeTag
import com.skedgo.android.common.model.Trip
import com.skedgo.android.tripkit.TripKit
import me.tatarka.bindingcollectionadapter2.ItemBinding
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject

class A2bTripsViewModel constructor(
    private val context: Context,
    private val getSegmentsSummary: GetSegmentsSummary
) {
  internal val onTripSelected: PublishSubject<Trip> = PublishSubject.create()
  private val _isRefreshing: BehaviorSubject<Boolean> = BehaviorSubject.create(false)
  val isRefreshing: Observable<Boolean> get() = _isRefreshing.asObservable()
  val trips: ObservableList<TripViewModel> = ObservableArrayList()
  val itemBinding: ItemBinding<TripViewModel> = ItemBinding.of(BR.viewModel, R.layout.trip)

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
      .flatMap { Observable.from(it) }
      .toSortedList { t1, t2 -> t1.displayTrip!!.endTimeInSecs.compareTo(t2.displayTrip!!.endTimeInSecs) }
      .doOnSubscribe { _isRefreshing.onNext(true) }
      .doOnUnsubscribe { _isRefreshing.onNext(false) }
      .observeOn(mainThread())
      .doOnNext { it.forEach { trips.add(TripViewModel(context, it, getSegmentsSummary, onTripSelected)) } }
      .map { Unit }
}
