package com.skedgo.android.common.agenda;

import com.skedgo.android.common.model.ITimeRange;
import com.skedgo.android.common.model.Location;

public interface ICalendarEvent extends ITimeRange {

  String getTitle();

  String getDescription();

  String getInstanceId();

  boolean isAllDay();

  Location getLocation();

  void setResolvedLocation(Location resolvedLocation);

  String getLocationString();

  boolean isLocationAmbiguous();

  void setAmbiguousLocationStatus(boolean ambiguousStatus);

  boolean isExcludedFromPlanning();

  void setExcludedFromPlanning(boolean excluded);

  /**
   * @return Whether this is internal event, which allows delete, modify, and vice versa
   */
  boolean isInternalEvent();

  ITimeRange getEffectiveTimeRange();

  void setEffectiveTimeRange(ITimeRange effectiveTimeRange);

  String getUID();
  int getId();
}