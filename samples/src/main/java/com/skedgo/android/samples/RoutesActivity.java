package com.skedgo.android.samples;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.tripkit.RouteOptions;
import com.skedgo.android.tripkit.TripKit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

public class RoutesActivity extends ListActivity {
  private ArrayAdapter<TripGroup> citiesAdapter;
  private View progressView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_routes);
    progressView = findViewById(R.id.progressView);

    citiesAdapter = new ArrayAdapter<TripGroup>(this, android.R.layout.simple_list_item_1) {
      @Override public View getView(int position, View convertView, ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext())
            .inflate(android.R.layout.simple_list_item_1, parent, false);

        final Trip displayTrip = getItem(position).getDisplayTrip();
        ((TextView) view.findViewById(android.R.id.text1)).setText(String.valueOf(displayTrip.getTimeCost()));

        return view;
      }
    };
    setListAdapter(citiesAdapter);

    // Two locations in Los Angeles, CA, USA.
    final Location departure = new Location(34.193984, -118.392930);
    departure.setAddress("11923 Vanowen St, North Hollywood, CA 91605, USA");
    final Location arrival = new Location(34.184923, -118.353576);
    arrival.setAddress("2001-2027 N Maple St, Burbank, CA 91505, USA");
    final RouteOptions routeOptions = new RouteOptions.Builder(departure, arrival)
        .departAfter(System.currentTimeMillis())
        .build();

    TripKit.with(getApplicationContext())
        .getRouteService()
        .routeAsync(routeOptions)
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
            citiesAdapter.add(route);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable error) {
            Toast.makeText(getApplicationContext(), "Error fetching routes", Toast.LENGTH_SHORT).show();
          }
        });
  }
}