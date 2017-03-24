package skedgo.tripkit.demo.a2btrips

import android.content.Context
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.view.View
import com.skedgo.android.common.model.TripSegment
import com.skedgo.android.common.util.TripSegmentUtils

class TripSegmentViewModel constructor(context: Context, segment: TripSegment) {
  val actionTitle by lazy {
    ObservableField<String>(TripSegmentUtils.getTripSegmentAction(context, segment))
  }

  val hasActionNotes by lazy {
    ObservableInt(if (!segment.notes.isNullOrEmpty()) View.VISIBLE else View.GONE)
  }

  val actionNotes by lazy {
    ObservableField<String>(segment.getDisplayNotes(context.resources))
  }
}
