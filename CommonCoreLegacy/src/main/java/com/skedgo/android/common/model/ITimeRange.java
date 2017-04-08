package com.skedgo.android.common.model;

public interface ITimeRange {
  long getStartTimeInSecs();
  void setStartTimeInSecs(long startTimeInSecs);
  long getEndTimeInSecs();
  void setEndTimeInSecs(long endTimeInSecs);
}