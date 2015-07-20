package com.skedgo.android.tripcoredemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripGroup;

import java.util.ArrayList;
import java.util.List;

public final class RoutesAdapter extends RecyclerView.Adapter<RouteViewHolder> {
  private final List<TripGroup> routes = new ArrayList<>();

  private RoutesAdapter() {}

  public static RoutesAdapter create() {
    return new RoutesAdapter();
  }

  public void addRoute(TripGroup route) {
    final int size = routes.size();
    routes.add(route);
    notifyItemInserted(size);
  }

  @Override
  public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View routeView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.view_route, parent, false);
    return new RouteViewHolder(routeView);
  }

  @Override
  public void onBindViewHolder(RouteViewHolder holder, int position) {
    final TripGroup route = routes.get(position);
    final Trip displayTrip = route.getDisplayTrip();
    holder.durationView.setText(String.valueOf(displayTrip.getTimeCost()));
  }

  @Override
  public int getItemCount() {
    return routes.size();
  }
}