package com.skedgo.android.common.model;

public enum GroupVisibility {
  FULL(1), COMPACT(0);

  /**
   * To be sortable.
   */
  public final int value;

  GroupVisibility(int value) {
    this.value = value;
  }
}
