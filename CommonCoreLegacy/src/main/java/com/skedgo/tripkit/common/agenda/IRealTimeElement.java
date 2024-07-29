package com.skedgo.tripkit.common.agenda;

/**
 * Signifies a class is able to fetch timetable information
 */
public interface IRealTimeElement {

    String getStartStopCode();

    void setStartStopCode(String startStopCode);

    String getEndStopCode();

    void setEndStopCode(String endStopCode);

    String getServiceTripId();

    String getOperator();

    long getStartTimeInSecs();
}