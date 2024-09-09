package com.skedgo.tripkit.data.database.locations.carparks

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "carParks")
class CarParkLocationEntity {

    @PrimaryKey
    var identifier: String = ""
    lateinit var cellId: String
    lateinit var name: String
    var lat: Double = 0.0
    var lng: Double = 0.0
    var address: String? = null
    var info: String? = null
    var modeIdentifier: String? = null
    lateinit var localIconName: String
    var remoteIconName: String? = null

    @Embedded
    lateinit var operator: ParkingOperatorEntity
}

@Entity(
    foreignKeys = [ForeignKey(
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
        entity = CarParkLocationEntity::class,
        parentColumns = ["identifier"], childColumns = ["carParkId"]
    )]
)
class OpeningDayEntity {
    @PrimaryKey
    var id: String = ""

    @ColumnInfo(index = true)
    lateinit var carParkId: String
    lateinit var name: String
}

@Entity(
    foreignKeys = [ForeignKey(
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
        entity = OpeningDayEntity::class,
        parentColumns = ["id"], childColumns = ["openingDayId"]
    )]
)
class OpeningTimeEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(index = true)
    lateinit var openingDayId: String
    lateinit var opens: String
    lateinit var closes: String
}

@Entity
class ParkingOperatorEntity {
    @ColumnInfo(name = "operator_name")
    lateinit var name: String
    var phone: String? = null
    var website: String? = null
}

@Entity(
    foreignKeys = [ForeignKey(
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
        entity = CarParkLocationEntity::class,
        parentColumns = ["identifier"], childColumns = ["carParkId"]
    )]
)
class PricingTableEntity {
    @PrimaryKey
    var id: String = ""
    lateinit var title: String
    var subtitle: String? = null
    lateinit var currencySymbol: String
    lateinit var currency: String

    @ColumnInfo(index = true)
    lateinit var carParkId: String
}

@Entity(
    foreignKeys = [ForeignKey(
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
        entity = PricingTableEntity::class,
        parentColumns = ["id"], childColumns = ["pricingTableId"]
    )]
)
class PricingEntryEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var price: Float = 0f
    var label: String? = null
    var maxDurationInMinutes: Int? = null

    @ColumnInfo(index = true)
    lateinit var pricingTableId: String
}