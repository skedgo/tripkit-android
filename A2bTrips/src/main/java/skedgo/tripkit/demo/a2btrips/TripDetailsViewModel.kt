package skedgo.tripkit.demo.a2btrips

import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.skedgo.android.common.model.Trip
import me.tatarka.bindingcollectionadapter2.ItemBinding
import rx.Observable

class TripDetailsViewModel constructor(context: Context, trip: Trip) {
  val segmentBinding = ItemBinding.of<TripSegmentViewModel>(BR.viewModel, R.layout.segment)
  val tripSegments: ObservableList<TripSegmentViewModel> = ObservableArrayList()

  init {
    Observable.from(trip.segments)
        .map { TripSegmentViewModel(context, it) }
        .toList()
        .subscribe {
          tripSegments.addAll(it)
        }
  }
}
