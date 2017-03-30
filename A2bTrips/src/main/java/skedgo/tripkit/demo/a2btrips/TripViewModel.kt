package skedgo.tripkit.demo.a2btrips

import android.content.Context
import android.databinding.ObservableField
import com.skedgo.android.common.model.Trip
import com.skedgo.android.common.model.TripGroup
import com.skedgo.android.common.model.Trips
import com.skedgo.android.common.util.DateTimeFormats
import skedgo.tripkit.routing.getSummarySegments
import rx.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class TripViewModel(
    private val context: Context,
    private val tripGroup: TripGroup,
    private val onTripSelected: PublishSubject<Trip>
) {
  private val representativeTrip: Trip by lazy { tripGroup.displayTrip!! }
  val modeInfos by lazy {
    representativeTrip.getSummarySegments()
        .map { it.modeInfo!!.alternativeText }
        .joinToString(separator = " > ")
  }

  val times by lazy {
    val departureMillis = TimeUnit.SECONDS.toMillis(representativeTrip.startTimeInSecs)
    val departureTime = DateTimeFormats.printTime(context, departureMillis, Trips.getDepartureTimezone(representativeTrip))

    val arrivalMillis = TimeUnit.SECONDS.toMillis(representativeTrip.endTimeInSecs)
    val arrivalTime = DateTimeFormats.printTime(context, arrivalMillis, Trips.getDepartureTimezone(representativeTrip))

    ObservableField<String>("Leave at $departureTime, arrive by $arrivalTime")
  }
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

  fun select() {
    onTripSelected.onNext(representativeTrip)
  }
}
