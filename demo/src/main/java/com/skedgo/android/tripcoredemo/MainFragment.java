package com.skedgo.android.tripcoredemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.tripkit.RouteOptions;
import com.skedgo.android.tripkit.RouteService;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainFragment extends Fragment {
  public MainFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

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
            // Show progress indicator here.
          }
        })
        .doOnTerminate(new Action0() {
          @Override public void call() {
            // Dismiss progress indicator here.
          }
        })
        .subscribe(new Action1<TripGroup>() {
          @Override public void call(TripGroup route) {
            // Add a newly fetched route into a ListAdapter for example.
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            // Show error here.
          }
        });
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_main, container, false);
  }
}