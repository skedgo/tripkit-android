package skedgo.tripkit.samples.a2brouting

import android.content.Context
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import me.tatarka.bindingcollectionadapter2.ItemBinding
import io.reactivex.Observable
import skedgo.tripkit.routing.Trip
import skedgo.tripkit.samples.BR
import skedgo.tripkit.samples.R

class TripDetailsViewModel constructor(context: Context, trip: Trip) {
  val itemBinding: ItemBinding<TripSegmentViewModel> = ItemBinding.of(BR.viewModel, R.layout.segment)
  val segments: ObservableList<TripSegmentViewModel> = ObservableArrayList()

  init {
    Observable.fromIterable(trip.segments)
        .map { TripSegmentViewModel(context, it) }
        .toList()
        .subscribe( {
          segments.addAll(it)
        }, {})
  }
}
