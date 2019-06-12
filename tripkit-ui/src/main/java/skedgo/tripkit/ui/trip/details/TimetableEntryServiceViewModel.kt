package skedgo.tripkit.ui.trip.details

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.jakewharton.rxrelay.PublishRelay
import com.skedgo.android.common.model.RealtimeAlert
import rx.Observable
import skedgo.tripkit.ui.R
import javax.inject.Inject

open class TimetableEntryServiceViewModel @Inject constructor(private val context: Context) {
  val title: ObservableField<String> = ObservableField()
  val hasAlerts: ObservableBoolean = ObservableBoolean(false)

  private val showAlert: PublishRelay<Unit> = PublishRelay.create()
  open val showAlertsObservable: Observable<Unit> = showAlert.asObservable()

  open fun setAlerts(alerts: List<RealtimeAlert>?) {
    with(alerts) {
      when {
        this == null || isEmpty() -> hasAlerts.set(false)
        size == 1 -> {
          hasAlerts.set(true)
          title.set(this[0].title())
        }
        else -> {
          hasAlerts.set(true)
          title.set("$size ${context.getString(R.string.alerts)}")
        }
      }
    }
  }

  fun onShow() {
    showAlert.call(Unit)
  }
}