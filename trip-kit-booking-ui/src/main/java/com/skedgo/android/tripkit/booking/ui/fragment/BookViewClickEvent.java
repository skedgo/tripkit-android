package com.skedgo.android.tripkit.booking.ui.fragment;

import android.support.annotation.NonNull;
import android.view.View;

import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.model.TripSegment;
import com.squareup.otto.Bus;

public class BookViewClickEvent implements View.OnClickListener {
  public final TripSegment segment;
  public final TripGroup group;
  private final Bus bus;

  public BookViewClickEvent(
      @NonNull Bus bus,
      @NonNull TripGroup group,
      @NonNull TripSegment segment) {
    this.bus = bus;
    this.group = group;
    this.segment = segment;
  }

  @Override
  public void onClick(View v) {
    bus.post(this);
  }
}