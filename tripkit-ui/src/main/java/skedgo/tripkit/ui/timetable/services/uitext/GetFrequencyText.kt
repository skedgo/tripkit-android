package skedgo.tripkit.ui.timetable.services.uitext

import android.annotation.SuppressLint
import android.content.Context
import com.skedgo.android.common.model.TimetableEntry
import com.skedgo.android.common.StyleManager
import skedgo.tripkit.ui.R
import javax.inject.Inject

open class GetFrequencyText @Inject constructor(
    private val context: Context
) {

  @SuppressLint("StringFormatInvalid")
  open fun execute(service: TimetableEntry): String {
    val firstString = when {
      service.serviceNumber.isNotEmpty() -> service.serviceNumber
      service.startStop?.name?.isNotEmpty() == true -> service.startStop.name
      else -> ""
    }
    val frequencyText = "${service.frequency} ${StyleManager.FORMAT_TIME_SPAN_MIN}"
    return context.getString(R.string._pattern_every__pattern, firstString, frequencyText)
  }
}