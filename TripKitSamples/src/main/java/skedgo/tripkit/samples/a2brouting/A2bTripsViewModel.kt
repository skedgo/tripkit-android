package skedgo.tripkit.samples.a2brouting

import android.content.Context
import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.Query
import com.skedgo.android.common.model.TimeTag
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import skedgo.tripkit.a2brouting.RouteService
import skedgo.tripkit.routing.Trip
import skedgo.tripkit.routing.TripGroupComparators
import skedgo.tripkit.samples.BR
import skedgo.tripkit.samples.R

class A2bTripsViewModel constructor(
    private val context: Context,
    private val routeService: RouteService
) {
  internal val onTripSelected: PublishSubject<Trip> = PublishSubject.create()
  private val _isRefreshing: BehaviorSubject<Boolean> = BehaviorSubject.create(false)
  val isRefreshing: Observable<Boolean> get() = _isRefreshing.asObservable()
  val items: DiffObservableList<TripViewModel> = DiffObservableList<TripViewModel>(GroupDiffCallback)
  val itemBinding: ItemBinding<TripViewModel> = ItemBinding.of(BR.viewModel, R.layout.trip)

  fun showSampleTrips(): Observable<Unit>
      = Observable
      .defer {
        val query = Query().apply {
          fromLocation = Location(34.193984, -118.392930).also {
            it.address = "11923 Vanowen St, North Hollywood, CA 91605, USA"
          }
          toLocation = Location(34.184923, -118.353576).also {
            it.address = "2001-2027 N Maple St, Burbank, CA 91505, USA"
          }
          setTimeTag(TimeTag.createForLeaveNow())
        }
        routeService.routeAsync(query)
      }
      .observeOn(mainThread())
      .scan { previous, new -> previous + new }
      .map { it.sortedWith(TripGroupComparators.ARRIVAL_COMPARATOR_CHAIN) }
      .doOnNext { TripGroupRepository.putAll(it) }
      .map { it.map { TripViewModel(context, it, onTripSelected) } }
      .map {
        Pair(it, items.calculateDiff(it))
      }
      .doOnSubscribe { _isRefreshing.onNext(true) }
      .doOnUnsubscribe { _isRefreshing.onNext(false) }
      .doOnNext { items.update(it.first, it.second) }
      .map { Unit }
}
