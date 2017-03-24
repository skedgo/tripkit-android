package skedgo.tripkit.demo.a2btrips

import android.content.Context
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.view.View
import com.skedgo.android.common.model.TripSegment
import com.skedgo.android.common.util.TripSegmentUtils


class TripSegmentViewModel constructor(context: Context, tripSegment: TripSegment) {

  val actionTitle by lazy {
    ObservableField<String>(TripSegmentUtils.getTripSegmentAction(context, tripSegment))
  }

  val hasActionNotes by lazy {
    ObservableInt(if (!tripSegment.notes.isNullOrEmpty()) View.VISIBLE else View.GONE)
  }

  val actionNotes by lazy {
    ObservableField<String>(tripSegment.getDisplayNotes(context.resources))
  }

}