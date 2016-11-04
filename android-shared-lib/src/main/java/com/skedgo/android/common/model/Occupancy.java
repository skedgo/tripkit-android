package com.skedgo.android.common.model;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.skedgo.android.common.model.Occupancy.CRUSHED_STANDING_ROOM_ONLY;
import static com.skedgo.android.common.model.Occupancy.EMPTY;
import static com.skedgo.android.common.model.Occupancy.FEW_SEATS_AVAILABLE;
import static com.skedgo.android.common.model.Occupancy.FULL;
import static com.skedgo.android.common.model.Occupancy.MANY_SEATS_AVAILABLE;
import static com.skedgo.android.common.model.Occupancy.NOT_ACCEPTING_PASSENGERS;
import static com.skedgo.android.common.model.Occupancy.STANDING_ROOM_ONLY;

@StringDef({EMPTY,
    MANY_SEATS_AVAILABLE,
    FEW_SEATS_AVAILABLE,
    STANDING_ROOM_ONLY,
    CRUSHED_STANDING_ROOM_ONLY,
    FULL,
    NOT_ACCEPTING_PASSENGERS})
@Retention(RetentionPolicy.SOURCE)
public @interface Occupancy {
  String EMPTY = "empty";
  String MANY_SEATS_AVAILABLE = "many_seats_available";
  String FEW_SEATS_AVAILABLE = "few_seats_available";
  String STANDING_ROOM_ONLY = "standing_room_only";
  String CRUSHED_STANDING_ROOM_ONLY = "crushed_standing_room_only";
  String FULL = "full";
  String NOT_ACCEPTING_PASSENGERS = "not_accepting_passengers";
}
