package skedgo.tripkit.samples.a2brouting

import android.content.Context
import androidx.databinding.ObservableField
import com.skedgo.TripKit
import com.skedgo.tripkit.routing.*
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
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
    val timeCost = ObservableField<String>()

    val moneyCost by lazy {
        val money = representativeTrip.moneyCost
        ObservableField<String>("Cost ${representativeTrip.currencySymbol}$money")
    }
    val co2Cost by lazy {
        ObservableField<String>("CO2: ${representativeTrip.carbonCost}kg")
    }

    init {
        val printTime = TripKit.getInstance().dateTimeComponent().printTime
        printTime.execute(representativeTrip.startDateTime)
                .withLatestFrom(printTime.execute(representativeTrip.endDateTime), BiFunction { startTimeText: String, endTimeText: String ->
                  "Leave at $startTimeText, arrive by $endTimeText"
                })
                .subscribe { times.set(it) }

      val duration = TimeUnit.SECONDS.toMinutes(representativeTrip.endTimeInSecs - representativeTrip.startTimeInSecs)

      timeCost.set("Take $duration minutes")
    }

    fun select() {
        onTripSelected.onNext(representativeTrip)
    }
}
