package com.skedgo.android.common.model;

public enum Availability {

  Available("AVAILABLE"),
  MissedPrebookingWindow("MISSED_PREBOOKING_WINDOW"),
  Cancelled("CANCELLED");

  private String value;

  Availability(String availability) {value = availability;}

  String getValue() {return value;}

  public static Availability fromString(String value) {
    for (Availability name : values()) {
      if (name.value.equals(value)) {
        return name;
      }
    }
    return null;
  }
}
