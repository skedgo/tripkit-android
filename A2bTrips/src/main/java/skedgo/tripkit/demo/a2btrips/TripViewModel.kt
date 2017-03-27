package skedgo.tripkit.demo.a2btrips

import android.content.Context
import android.databinding.ObservableField
import android.util.Log
import com.skedgo.android.common.model.Trip
import com.skedgo.android.common.model.TripGroup
import com.skedgo.android.common.model.Trips
import com.skedgo.android.common.util.DateTimeFormats
import me.tatarka.bindingcollectionadapter2.ItemBinding
import rx.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class TripViewModel constructor(
    private val context: Context,
    private val tripGroup: TripGroup,
    private val getSegmentSummary: GetSegmentsSummary,
    private val onTripSelected: PublishSubject<Trip>
) {

  private val representativeTrip: Trip by lazy { tripGroup.displayTrip!! }
  val modeInfos: MutableList<String> = mutableListOf()
  val itemBinding: ItemBinding<String> = ItemBinding.of(BR.viewModel, R.layout.segment_mode_info)

  init {
    getSegmentSummary.execute(representativeTrip)
        .subscribe(
            { modeInfos.add(it) }, { Log.d("TripViewModel", it.toString()) }
        )
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
