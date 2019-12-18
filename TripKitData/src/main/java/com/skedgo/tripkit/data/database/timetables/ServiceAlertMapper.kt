package com.skedgo.tripkit.data.database.timetables

import com.skedgo.tripkit.common.model.*
import java.util.*
import javax.inject.Inject

class ServiceAlertMapper @Inject constructor() {

  fun toEntity(serviceTripId: String, realtimeAlert: RealtimeAlert): ServiceAlertsEntity {
    return with(realtimeAlert) {
      ServiceAlertsEntity(
          id = UUID.randomUUID().toString(),
          serviceTripId = serviceTripId,
          location = this.location()?.let {
            AlertLocationEntity(
                lat = it.lat,
                lng = it.lon,
                timezone = it.timeZone!!,
                address = it.address)
          },
          action = this.alertAction()?.let {
            AlertActionEntity(
                text = it.text().orEmpty(),
                type = it.type().orEmpty(),
                excludedStopCodes = it.excludedStopCodes().orEmpty().joinToString(separator = ","))
          },
          text = this.text(),
          fromDate = this.fromDate(),
          remoteHashCode = this.remoteHashCode(),
          lastUpdated = this.lastUpdated(),
          remoteIcon = this.remoteIcon(),
          severity = this.severity()!!,
          title = this.title()!!,
          url = this.url()
      )
    }
  }

  fun toModel(serviceAlertsEntity: ServiceAlertsEntity): RealtimeAlert {
    return with(serviceAlertsEntity) {
      val location: Location? = this.location?.let {
        Location(it.lat, it.lng).apply {
          this.address = it.address
          this.timeZone = it.timezone
        }
      }
      val action: AlertAction? = this.action?.let {
        ImmutableAlertAction.builder()
            .text(it.text)
            .type(it.type)
            .addAllExcludedStopCodes(it.excludedStopCodes.split(","))
            .build()
      }
      ImmutableRealtimeAlert.builder()
          .text(this.text)
          .fromDate(this.fromDate)
          .remoteHashCode(this.remoteHashCode)
          .lastUpdated(this.lastUpdated)
          .remoteIcon(this.remoteIcon)
          .severity(this.severity)
          .title(this.title)
          .url(this.url)
          .location(location)
          .alertAction(action)
          .build()
    }
  }

}