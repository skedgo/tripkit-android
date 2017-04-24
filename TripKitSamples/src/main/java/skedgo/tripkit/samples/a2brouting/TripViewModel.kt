package skedgo.tripkit.samples.a2brouting

import android.content.Context
import android.databinding.ObservableField
import com.skedgo.android.tripkit.TripKit
import rx.subjects.PublishSubject
import skedgo.tripkit.routing.*
import java.util.concurrent.TimeUnit

class TripViewModel(
    private val context: Context,
    val group: TripGroup,
    private val onTripSelected: PublishSubject<Trip>
) {
  private val representativeTrip: Trip by lazy { group.displayTrip!! }
  val modeInfos by lazy {
    representativeTrip.getSummarySegments()
        .map { it.modeInfo!!.alternativeText }
        .joinToString(separator = " > ")
  }

  val times = ObservableField<String?>()
  val timeCost by lazy {
    val duration = TimeUnit.SECONDS.toMinutes(representativeTrip.endTimeInSecs - representativeTrip.startTimeInSecs)
    ObservableField<String>("Take $duration minutes")
  }
  val moneyCost by lazy {
    val money = representativeTrip.moneyCost
    ObservableField<String>("Cost ${representativeTrip.currencySymbol}$money")
  }
  val co2Cost by lazy {
    ObservableField<String>("CO2: ${representativeTrip.carbonCost}kg")
  }

  init {
    val printTime = TripKit.getInstance().dateTimeComponent().printTime()
    printTime.execute(representativeTrip.startDateTime)
        .withLatestFrom(printTime.execute(representativeTrip.endDateTime)) {
          startTimeText, endTimeText ->
          "Leave at $startTimeText, arrive by $endTimeText"
        }
        .subscribe { times.set(it) }
  }

  fun select() {
    onTripSelected.onNext(representativeTrip)
  }
}
