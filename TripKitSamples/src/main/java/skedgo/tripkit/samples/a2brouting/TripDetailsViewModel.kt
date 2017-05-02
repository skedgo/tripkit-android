package skedgo.tripkit.samples.a2brouting

import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import skedgo.tripkit.routing.Trip
import me.tatarka.bindingcollectionadapter2.ItemBinding
import rx.Observable
import skedgo.tripkit.samples.BR
import skedgo.tripkit.samples.R

class TripDetailsViewModel constructor(context: Context, trip: Trip) {
  val itemBinding: ItemBinding<TripSegmentViewModel> = ItemBinding.of(BR.viewModel, R.layout.segment)
  val segments: ObservableList<TripSegmentViewModel> = ObservableArrayList()

  init {
    Observable.from(trip.segments)
        .map { TripSegmentViewModel(context, it) }
        .toList()
        .subscribe {
          segments.addAll(it)
        }
  }
}
