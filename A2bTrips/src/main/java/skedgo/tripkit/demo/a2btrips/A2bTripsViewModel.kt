package skedgo.tripkit.demo.a2btrips

import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.skedgo.android.common.model.Trip
import com.skedgo.android.common.model.TripGroupComparators
import com.skedgo.android.tripkit.TripKit
import me.tatarka.bindingcollectionadapter2.ItemBinding
import org.joda.time.DateTime
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import skedgo.tripkit.routing.a2b.A2bRoutingRequest
import skedgo.tripkit.routing.a2b.RequestTime.DepartNow

class A2bTripsViewModel constructor(
    private val context: Context
) {
  internal val onTripSelected: PublishSubject<Trip> = PublishSubject.create()
  private val _isRefreshing: BehaviorSubject<Boolean> = BehaviorSubject.create(false)
  val isRefreshing: Observable<Boolean> get() = _isRefreshing.asObservable()
  val trips: ObservableList<TripViewModel> = ObservableArrayList()
  val itemBinding: ItemBinding<TripViewModel> = ItemBinding.of(BR.viewModel, R.layout.trip)

  fun showSampleTrips(): Observable<Unit>
      = Observable
      .defer {
        TripKit.singleton().getA2bTrips.execute(
            A2bRoutingRequest.builder()
                .origin(Pair(34.193984, -118.392930))
                .originAddress("11923 Vanowen St, North Hollywood, CA 91605, USA")
                .destination(Pair(34.184923, -118.353576))
                .destinationAddress("2001-2027 N Maple St, Burbank, CA 91505, USA")
                .time(DepartNow { DateTime.now() })
                .build()
        )
      }
      .flatMap { Observable.from(it) }
      .toSortedList { lhs, rhs -> TripGroupComparators.ARRIVAL_COMPARATOR_CHAIN.compare(lhs, rhs) }
      .doOnSubscribe { _isRefreshing.onNext(true) }
      .doOnUnsubscribe { _isRefreshing.onNext(false) }
      .observeOn(mainThread())
      .doOnNext { trips.addAll(it.map { TripViewModel(context, it, onTripSelected) }) }
      .map { Unit }
}
