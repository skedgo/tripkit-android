package com.skedgo.android.common.model;

/**
 * Thuy's remark: This should have been {@link ServiceStop}.
 * We parse network response into ServiceStops,
 * then persist them into SQLite database.
 * However, when loading, we use such {@link StopInfo} to indicate service' stops.
 */
public class StopInfo {
  public final int id;
  public final RealTimeStatus realTimeStatus;
  public final boolean sortByArrive;
  public final ServiceStop stop;
  public final int serviceColor;
  public final boolean travelled;

  public StopInfo(int id,
                  RealTimeStatus realTimeStatus,
                  boolean sortByArrive,
                  ServiceStop stop,
                  int serviceColor, boolean travelled) {
    this.id = id;
    this.realTimeStatus = realTimeStatus;
    this.sortByArrive = sortByArrive;
    this.stop = stop;
    this.serviceColor = serviceColor;
    this.travelled = travelled;
  }
}