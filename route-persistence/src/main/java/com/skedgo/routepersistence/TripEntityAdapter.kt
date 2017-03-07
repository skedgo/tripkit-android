package com.skedgo.routepersistence

import android.content.ContentValues
import android.database.Cursor
import com.skedgo.android.common.model.Trip
import com.skedgo.routepersistence.TripGroupContract.COL_ARRIVE
import com.skedgo.routepersistence.TripGroupContract.COL_CALORIES_COST
import com.skedgo.routepersistence.TripGroupContract.COL_CARBON_COST
import com.skedgo.routepersistence.TripGroupContract.COL_CURRENCY_SYMBOL
import com.skedgo.routepersistence.TripGroupContract.COL_DEPART
import com.skedgo.routepersistence.TripGroupContract.COL_GROUP_ID
import com.skedgo.routepersistence.TripGroupContract.COL_HASSLE_COST
import com.skedgo.routepersistence.TripGroupContract.COL_ID
import com.skedgo.routepersistence.TripGroupContract.COL_MONEY_COST
import com.skedgo.routepersistence.TripGroupContract.COL_PLANNED_URL
import com.skedgo.routepersistence.TripGroupContract.COL_PROGRESS_URL
import com.skedgo.routepersistence.TripGroupContract.COL_QUERY_IS_LEAVE_AFTER
import com.skedgo.routepersistence.TripGroupContract.COL_SAVE_URL
import com.skedgo.routepersistence.TripGroupContract.COL_TEMP_URL
import com.skedgo.routepersistence.TripGroupContract.COL_UPDATE_URL
import com.skedgo.routepersistence.TripGroupContract.COL_UUID
import com.skedgo.routepersistence.TripGroupContract.COL_WEIGHTED_SCORE
import skedgo.sqlite.SQLiteEntityAdapter


class TripEntityAdapter : SQLiteEntityAdapter<Trip> {

  override fun toContentValues(trip: Trip): ContentValues {
    val values = ContentValues()
    values.put(COL_ID, trip.getId())
    values.put(COL_UUID, trip.uuid())
    values.put(COL_CURRENCY_SYMBOL, trip.getCurrencySymbol())
    values.put(COL_SAVE_URL, trip.getSaveURL())
    values.put(COL_DEPART, trip.getStartTimeInSecs())
    values.put(COL_ARRIVE, trip.getEndTimeInSecs())
    values.put(COL_CALORIES_COST, trip.getCaloriesCost())
    values.put(COL_MONEY_COST, trip.getMoneyCost())
    values.put(COL_CARBON_COST, trip.getCarbonCost())
    values.put(COL_HASSLE_COST, trip.getHassleCost())
    values.put(COL_WEIGHTED_SCORE, trip.getWeightedScore())
    values.put(COL_UPDATE_URL, trip.getUpdateURL())
    values.put(COL_PROGRESS_URL, trip.getProgressURL())
    values.put(COL_PLANNED_URL, trip.getPlannedURL())
    values.put(COL_TEMP_URL, trip.getTemporaryURL())
    values.put(COL_QUERY_IS_LEAVE_AFTER, if (trip.queryIsLeaveAfter()) 1 else 0)
    return values
  }

  override fun toEntity(cursor: Cursor): Trip {
    val id = cursor.getLong(cursor.getColumnIndex(COL_ID))
    val uuid = cursor.getString(cursor.getColumnIndex(COL_UUID))
    val currencySymbol = cursor.getString(cursor.getColumnIndex(COL_CURRENCY_SYMBOL))
    val saveUrl = cursor.getString(cursor.getColumnIndex(COL_SAVE_URL))
    val depart = cursor.getLong(cursor.getColumnIndex(COL_DEPART))
    val arrive = cursor.getLong(cursor.getColumnIndex(COL_ARRIVE))
    val caloriesCost = cursor.getFloat(cursor.getColumnIndex(COL_CALORIES_COST))
    val moneyCost = cursor.getFloat(cursor.getColumnIndex(COL_MONEY_COST))
    val carbonCost = cursor.getFloat(cursor.getColumnIndex(COL_CARBON_COST))
    val hassleCost = cursor.getFloat(cursor.getColumnIndex(COL_HASSLE_COST))
    val weightedScore = cursor.getFloat(cursor.getColumnIndex(COL_WEIGHTED_SCORE))
    val updateUrl = cursor.getString(cursor.getColumnIndex(COL_UPDATE_URL))
    val progressUrl = cursor.getString(cursor.getColumnIndex(COL_PROGRESS_URL))
    val plannedUrl = cursor.getString(cursor.getColumnIndex(COL_PLANNED_URL))
    val tempUrl = cursor.getString(cursor.getColumnIndex(COL_TEMP_URL))
    val queryIsLeaveAfter = cursor.getInt(cursor.getColumnIndex(COL_QUERY_IS_LEAVE_AFTER))

    val trip = Trip()
    trip.id = id
    trip.uuid(uuid)
    trip.currencySymbol = currencySymbol
    trip.saveURL = saveUrl
    trip.startTimeInSecs = depart
    trip.endTimeInSecs = arrive
    trip.caloriesCost = caloriesCost
    trip.moneyCost = moneyCost
    trip.carbonCost = carbonCost
    trip.hassleCost = hassleCost
    trip.weightedScore = weightedScore
    trip.setUpdateURL(updateUrl)
    trip.progressURL = progressUrl
    trip.plannedURL = plannedUrl
    trip.temporaryURL = tempUrl
    trip.setQueryIsLeaveAfter(queryIsLeaveAfter == 1)
    return trip
  }
}