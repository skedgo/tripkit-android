package com.skedgo.tripkit.data.database.stops

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skedgo.tripkit.common.model.ScheduledStop
import com.skedgo.tripkit.common.model.StopType
import com.skedgo.tripkit.data.database.locations.bikepods.ModeInfoEntity
import com.skedgo.tripkit.data.database.locations.bikepods.ServiceColorEntity
import com.skedgo.tripkit.routing.ModeInfo
import com.skedgo.tripkit.routing.ServiceColor

@Entity(tableName = "stopLocations")
class StopLocationEntity {

    var address: String? = null

    @PrimaryKey
    var code: String = ""
    lateinit var name: String
    var popularify: Int = 0
    lateinit var services: String
    lateinit var stopType: String
    var timeZone: String? = null
    var wheelchairAccessible: Boolean = false
    var bicycleAccessible: Boolean = false

    var lat: Double = 0.0
    var lng: Double = 0.0

    @Embedded(prefix = "modeinfo_")
    lateinit var modeInfo: ModeInfoEntity
}

fun StopLocationEntity.toScheduledStop(): ScheduledStop {
    return ScheduledStop().let {
        it.address = this.address
        it.code = this.code
        it.popularity = this.popularify
        it.name = this.name
        it.services = this.services
        it.type = this.stopType.let { StopType.from(it) }
        it.timeZone = this.timeZone
        it.wheelchairAccessible = this.wheelchairAccessible
        it.bicycleAccessible = this.bicycleAccessible
        it.lat = this.lat
        it.lon = this.lng
        it.modeInfo = this.modeInfo.toModeInfo()
        it
    }
}

fun ModeInfoEntity.toModeInfo(): ModeInfo {
    return ModeInfo().let {
        it.alternativeText = this.alt.orEmpty()
        it.description = this.description
        it.id = this.identifier
        it.localIconName = this.localIcon
        it.remoteDarkIconName = this.remoteDarkIcon
        it.remoteIconName = this.remoteIcon
        it.remoteIconIsTemplate = this.remoteIconIsTemplate
        it.remoteIconIsBranding = this.remoteIconIsBranding
        it.color = this.color?.let { ServiceColor(it.red, it.green, it.blue) }
        it
    }
}

fun ModeInfo.toEntity(): ModeInfoEntity {
    return ModeInfoEntity().let {
        it.alt = this.alternativeText
        it.description = this.description
        it.identifier = this.id
        it.localIcon = this.localIconName
        it.remoteDarkIcon = this.remoteDarkIconName
        it.remoteIcon = this.remoteIconName
        it.remoteIconIsTemplate = this.remoteIconIsTemplate
        it.remoteIconIsBranding = this.remoteIconIsBranding
        it.color = this.color?.let {
            ServiceColorEntity().apply {
                this.red = it.red
                this.green = it.green
                this.blue = it.blue
            }
        }
        it
    }
}

fun ScheduledStop.toEntity(): StopLocationEntity {
    return StopLocationEntity().let {
        it.address = this.address
        it.code = this.code
        it.popularify = this.popularity
        it.name = this.name
        it.services = this.services
        it.stopType = this.type.name
        it.timeZone = this.timeZone
        it.wheelchairAccessible = this.wheelchairAccessible ?: false
        it.lat = this.lat
        it.lng = this.lon
        it.modeInfo = this.modeInfo.toEntity()
        it
    }
}