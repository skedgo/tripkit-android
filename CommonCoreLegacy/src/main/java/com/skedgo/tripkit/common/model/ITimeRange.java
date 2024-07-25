package com.skedgo.tripkit.common.model;

public interface ITimeRange {
    long getStartTimeInSecs();

    void setStartTimeInSecs(long startTimeInSecs);

    long getEndTimeInSecs();

    void setEndTimeInSecs(long endTimeInSecs);
}