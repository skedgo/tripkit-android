package com.skedgo.tripkit.data.database.locations.bikepods


import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "bikepods")
@Keep
class BikePodLocationEntity {
    @PrimaryKey
    var identifier: String = ""
    var cellId: String? = null
    var lat: Double = 0.0
    var lng: Double = 0.0
    var address: String? = null
    var timezone: String? = null

    @Embedded
    lateinit var bikePod: BikePodEntity

    @Embedded(prefix = "modeinfo_")
    var modeInfo: ModeInfoEntity? = null
}

@Entity
@Keep
class BikePodEntity {
    @Ignore
    lateinit var identifier: String
    var inService: Boolean? = false
    var totalSpaces: Int? = 0
    var availableBikes: Int? = 0
    var lastUpdate: Long? = 0
    var deepLink: String? = null

    @Embedded(prefix = "operator_")
    lateinit var operator: Operator

    @Embedded(prefix = "datasource_")
    var dataSource: DataSourceAttribution? = null
}

@Entity
@Keep
class Operator {
    lateinit var name: String
    var website: String? = null
    var phone: String? = null

    @Embedded
    var appInfo: AppInfoEntity? = null
}

@Entity
@Keep
class AppInfoEntity {
    var appURLAndroid: String? = null
}

@Entity
@Keep
class DataSourceAttribution {
    var disclaimer: String? = null

    @Embedded
    var provider: CompanyInfo? = null
}

@Entity
@Keep
class CompanyInfo {
    lateinit var name: String
    var phone: String? = null
    var website: String? = null
}

@Entity
@Keep
class ModeInfoEntity {
    var identifier: String? = null
    var alt: String? = null
    var localIcon: String? = null
    var remoteIcon: String? = null
    var remoteDarkIcon: String? = null

    var description: String? = null
    var remoteIconIsTemplate: Boolean = false
    var remoteIconIsBranding: Boolean = false

    @Embedded(prefix = "serviceColor_")
    var color: ServiceColorEntity? = null
}

@Keep
@Entity
class ServiceColorEntity {
    var red: Int = 0
    var blue: Int = 0
    var green: Int = 0
}