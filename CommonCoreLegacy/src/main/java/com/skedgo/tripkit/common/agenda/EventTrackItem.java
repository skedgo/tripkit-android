package com.skedgo.tripkit.common.agenda;

import com.google.gson.annotations.SerializedName;
import com.skedgo.tripkit.common.model.ITimeRange;

public class EventTrackItem extends TrackItem implements ITimeRange {
    @SerializedName("effectiveStart")
    private long effectiveStart;
    @SerializedName("effectiveEnd")
    private long effectiveEnd;

    @Override
    public long getStartTimeInSecs() {
        return effectiveStart;
    }

    @Override
    public void setStartTimeInSecs(long startTimeInSecs) {
    }

    @Override
    public long getEndTimeInSecs() {
        return effectiveEnd;
    }

    @Override
    public void setEndTimeInSecs(long endTimeInSecs) {
    }
}