package com.skedgo.android.tripcoredemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RouteViewHolder extends RecyclerView.ViewHolder {
  public final TextView durationView;

  public RouteViewHolder(View itemView) {
    super(itemView);
    durationView = ((TextView) itemView.findViewById(R.id.durationView));
  }
}