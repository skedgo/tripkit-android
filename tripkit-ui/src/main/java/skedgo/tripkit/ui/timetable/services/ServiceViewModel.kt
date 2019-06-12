package skedgo.tripkit.ui.timetable.services

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.databinding.ObservableInt
import skedgo.tripkit.ui.util.TapAction
import com.skedgo.android.common.model.TimetableEntry
import skedgo.tripkit.ui.timetable.services.uitext.GetRealtimeText
import skedgo.tripkit.ui.timetable.services.uitext.GetServiceSubTitleText
import skedgo.tripkit.ui.timetable.services.uitext.GetServiceTitleText

import skedgo.tripkit.ui.trip.details.OccupancyViewModel
import skedgo.tripkit.ui.trip.details.TimetableEntryServiceViewModel
import skedgo.tripkit.ui.trip.details.WheelchairAvailabilityViewModel

import com.skedgo.android.common.model.RealtimeAlert
import org.joda.time.DateTimeZone
import rx.android.schedulers.AndroidSchedulers
import skedgo.tripkit.routing.ModeInfo
import skedgo.tripkit.ui.R
import skedgo.tripkit.ui.core.ReactiveViewModel
import skedgo.tripkit.ui.util.TimeSpanUtils
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface ServiceViewModel {
  val occupancyViewModel: OccupancyViewModel
  val wheelchairAvailabilityViewModel: WheelchairAvailabilityViewModel
  val timetableEntryServiceViewModel: TimetableEntryServiceViewModel
  val primaryText: ObservableField<String>
  val secondaryText: ObservableField<String>
  val tertiaryText: ObservableField<String>
  val countDownTimeText: ObservableField<String>
  val alpha: ObservableFloat
  val serviceColorViewVisible: ObservableBoolean
  val serviceAlertViewVisible: ObservableBoolean
  val countDownTimeTextBack: ObservableField<Drawable>
  val serviceColor: ObservableInt
  val modeInfo: ObservableField<ModeInfo>
  val onItemClick: TapAction<TimetableEntry>
  val onAlertsClick: TapAction<ArrayList<RealtimeAlert>>

  var service: TimetableEntry
  var dateTimeZone: DateTimeZone

  fun getRealTimeDeparture(): Long
  fun setService(_service: TimetableEntry, _dateTimeZone: DateTimeZone)
  fun updateService()

}

internal class ServiceViewModelImpl @Inject constructor(
    private val context: Context,
    override val occupancyViewModel: OccupancyViewModel,
    override val wheelchairAvailabilityViewModel: WheelchairAvailabilityViewModel,
    override val timetableEntryServiceViewModel: TimetableEntryServiceViewModel,
    private val getServiceTitleText: GetServiceTitleText,
    private val getServiceSubTitleText: GetServiceSubTitleText,
    private val getRealtimeText: GetRealtimeText
) : ReactiveViewModel(), ServiceViewModel {

  override val primaryText = ObservableField<String>()
  override val secondaryText = ObservableField<String>()
  override val tertiaryText = ObservableField<String>()
  override val countDownTimeText = ObservableField<String>()
  override val alpha = ObservableFloat(1f)
  override val serviceColorViewVisible = ObservableBoolean(false)
  override val serviceAlertViewVisible = ObservableBoolean(false)
  override val countDownTimeTextBack: ObservableField<Drawable> = ObservableField()
  override val serviceColor: ObservableInt = ObservableInt()
  override val modeInfo: ObservableField<ModeInfo> = ObservableField()
  override val onItemClick = TapAction.create { service }

  override val onAlertsClick = TapAction.create { service.alerts }

  override lateinit var service: TimetableEntry
  override lateinit var dateTimeZone: DateTimeZone

  override fun getRealTimeDeparture() = realTimeDeparture(service, service.realtimeVehicle)

  override fun updateService() {
    setService(service, dateTimeZone)
  }

  override fun setService(_service: TimetableEntry,
                          _dateTimeZone: DateTimeZone) {
    service = _service
    dateTimeZone = _dateTimeZone

    updateInfo()
  }

  private fun updateInfo() {
    modeInfo.set(service.modeInfo)

    serviceAlertViewVisible.set(service.hasAlerts())

    presentPrimaryText()

    presentSecondaryText()

    presentTertiaryText()

    presentOccupancy()

    presentCountDownTimeForFrequency()

    presentServiceColor(service)

    initHelpersVMs(service)
  }

  private fun presentPrimaryText() {
    getServiceTitleText.execute(service, dateTimeZone, service.realtimeVehicle)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          primaryText.set(it)
        }, { Log.e("ServiceViewModel", "Error: $it") })
        .autoClear()
  }

  private fun presentSecondaryText() {
    getServiceSubTitleText.execute(service, dateTimeZone)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          secondaryText.set(it)
        }, { Log.e("ServiceViewModel", "Error: $it") })
        .autoClear()
  }

  private fun presentTertiaryText() {
    getRealtimeText.execute(dateTimeZone, service, service.realtimeVehicle)
        .subscribe { tertiaryText.set(it) }
        .autoClear()
  }

  private fun presentOccupancy() {
    service.realtimeVehicle?.let { occupancyViewModel.setOccupancy(it, false) }
  }

  private fun presentCountDownTimeForFrequency() {
    if (!service.isFrequencyBased) {
      service.getTimeLeftToDepartInterval(1, TimeUnit.MINUTES, service.realtimeVehicle)
          .subscribe({ presentCountDownTime(it) }, { Log.e("ServiceViewModel", "Error: $it") })
          .autoClear()
    }
  }

  private fun presentCountDownTime(departureCountDownTimeInMins: Long) {
    countDownTimeText.set(TimeSpanUtils.getRelativeTimeSpanString(departureCountDownTimeInMins))

    if (departureCountDownTimeInMins < 0) {
      countDownTimeTextBack.set(ContextCompat.getDrawable(context, R.drawable.v4_shape_rect_cancelled))
      alpha.set(0.5f)
    } else {
      countDownTimeTextBack.set(ContextCompat.getDrawable(context, R.drawable.v4_shape_btn_positive_normal))
      alpha.set(1f)
    }
  }

  private fun presentServiceColor(service: TimetableEntry) {
    service.serviceColor?.let {
      when (it.color) {
        Color.BLACK, Color.WHITE -> {
          serviceColorViewVisible.set(false)
        }
        else -> {
          serviceColorViewVisible.set(true)
          serviceColor.set(it.color)
        }
      }
    }
  }

  private fun initHelpersVMs(service: TimetableEntry) {
    wheelchairAvailabilityViewModel.setService(service)
    timetableEntryServiceViewModel.setAlerts(service.alerts)
    timetableEntryServiceViewModel.showAlertsObservable
        .subscribe { onAlertsClick.perform() }
        .autoClear()
  }
}