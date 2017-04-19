package skedgo.tripkit.samples.a2brouting

import android.content.Context
import com.skedgo.android.common.model.Trip
import com.skedgo.android.common.model.TripGroupComparators
import com.skedgo.android.tripkit.TripKit
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList
import org.joda.time.DateTime
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import skedgo.tripkit.a2brouting.A2bRoutingRequest
import skedgo.tripkit.a2brouting.RequestTime.DepartNow
import skedgo.tripkit.samples.BR
import skedgo.tripkit.samples.R

class A2bTripsViewModel constructor(
    private val context: Context
) {
  internal val onTripSelected: PublishSubject<Trip> = PublishSubject.create()
  private val _isRefreshing: BehaviorSubject<Boolean> = BehaviorSubject.create(false)
  val isRefreshing: Observable<Boolean> get() = _isRefreshing.asObservable()
  val items: DiffObservableList<TripViewModel> = DiffObservableList<TripViewModel>(GroupDiffCallback)
  val itemBinding: ItemBinding<TripViewModel> = ItemBinding.of(BR.viewModel, R.layout.trip)

  fun showSampleTrips(): Observable<Unit>
      = Observable
      .defer {
        TripKit.getInstance().getA2bRoutingResults.execute(
            A2bRoutingRequest.builder()
                .origin(Pair(34.193984, -118.392930))
                .originAddress("11923 Vanowen St, North Hollywood, CA 91605, USA")
                .destination(Pair(34.184923, -118.353576))
                .destinationAddress("2001-2027 N Maple St, Burbank, CA 91505, USA")
                .time(DepartNow { DateTime.now() })
                .build()
        )
      }
      .scan { previous, new -> previous + new }
      .map { it.sortedWith(TripGroupComparators.ARRIVAL_COMPARATOR_CHAIN) }
      .map { it.map { TripViewModel(context, it, onTripSelected) } }
      .map {
        Pair(it, items.calculateDiff(it))
      }
      .doOnSubscribe { _isRefreshing.onNext(true) }
      .doOnUnsubscribe { _isRefreshing.onNext(false) }
      .observeOn(mainThread())
      .doOnNext { items.update(it.first, it.second) }
      .map { Unit }
}
