package skedgo.tripkit.samples.a2brouting

import android.content.Context
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import android.view.View
import com.skedgo.tripkit.common.util.TripSegmentUtils
import com.skedgo.tripkit.routing.TripSegment

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
