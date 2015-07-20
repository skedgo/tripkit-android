package com.skedgo.android.tripcoredemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.tripcore.RouteOptions;
import com.skedgo.android.tripcore.RouteService;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RoutesFragment extends Fragment {
  private final RoutesAdapter routesAdapter = RoutesAdapter.create();
  private View progressView;

  public RoutesFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_routes, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    progressView = view.findViewById(R.id.progressView);
    final RecyclerView routesView = (RecyclerView) view.findViewById(R.id.routesView);
    routesView.setLayoutManager(new LinearLayoutManager(getActivity()));
    routesView.setAdapter(routesAdapter);

    route();
  }

  private void route() {
    final Location departure = new Location(-33.8599089, 151.2071104);
    final Location arrival = new Location(-33.876114, 151.205526);

    final RouteOptions routeOptions = new RouteOptions.Builder(departure, arrival)
        .departAfter(System.currentTimeMillis())
        .build();

    final RouteService routeService = AwesomeApp.getRouteComponent(getActivity())
        .provideRouteService();
    routeService.routeAsync(routeOptions)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnRequest(new Action1<Long>() {
          @Override public void call(Long unused) {
            progressView.setVisibility(View.VISIBLE);
          }
        })
        .doOnTerminate(new Action0() {
          @Override public void call() {
            progressView.setVisibility(View.GONE);
          }
        })
        .subscribe(new Action1<TripGroup>() {
          @Override public void call(TripGroup route) {
            routesAdapter.addRoute(route);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            // Show error here.
          }
        });
  }
}