package skedgo.tripgo.parkingspots.models

import com.skedgo.tripkit.parkingspots.models.PaymentType

class OnStreetParkingDetails(
    val actualRate: String?,
    val availableSpaces: Int?,
    val totalSpaces: Int?,
    val lastUpdate: Long?,
    val restrictions: List<Restriction>?,
    val paymentTypes: List<PaymentType>?)

class Restriction(val color: String,
                  val maximumParkingMinutes: Int?,
                  val parkingSymbol: String,
                  val type: String,
                  val timezone: String,
                  val daysAndTimes: List<RestrictionDayAndTime>)

class RestrictionDayAndTime(val name: String, val times: List<RestrictionTime>)

class RestrictionTime(val closes: String, val opens: String)