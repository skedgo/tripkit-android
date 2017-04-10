package com.skedgo.android.tripkit;

public final class RoutingUserError extends RuntimeException {
  public RoutingUserError(String detailMessage) {
    super(detailMessage);
  }
}