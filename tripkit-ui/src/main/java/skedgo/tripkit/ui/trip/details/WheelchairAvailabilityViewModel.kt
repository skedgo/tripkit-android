package skedgo.tripkit.ui.trip.details

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.skedgo.android.common.model.TimetableEntry
import skedgo.tripkit.ui.R
import javax.inject.Inject

open class WheelchairAvailabilityViewModel @Inject constructor(private val context: Context) {
  val title: ObservableField<String> = ObservableField()
  val textColor = ObservableInt()
  val image = ObservableField<Drawable>()
  val hasWheelchairInformation = ObservableBoolean()

  open fun setService(service: TimetableEntry) {
    when (service.wheelchairAccessible) {
      null -> {
        hasWheelchairInformation.set(false)
      }
      true -> {
        hasWheelchairInformation.set(true)
        image.set(ContextCompat.getDrawable(context, R.drawable.ic_wheelchair_accessible))
        title.set(context.getString(R.string.wheelchair_accessible))
        textColor.set(ContextCompat.getColor(context, R.color.light_blue))
      }
      false -> {
        hasWheelchairInformation.set(true)
        image.set(ContextCompat.getDrawable(context, R.drawable.ic_wheelchair_inacessible))
        title.set(context.getString(R.string.not_wheelchair_accessible))
        textColor.set(ContextCompat.getColor(context, R.color.light_grey_4))
      }
    }
  }
}