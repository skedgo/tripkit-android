package com.skedgo.android.samples;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.tripkit.RegionService;
import com.skedgo.android.tripkit.tsp.Paratransit;

import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class CitiesActivity extends ListActivity {
  private ArrayAdapter<Location> citiesAdapter;
  private RegionService regionService;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    citiesAdapter = new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1) {
      @Override public View getView(int position, View convertView, ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext())
            .inflate(android.R.layout.simple_list_item_1, parent, false);
        final String cityName = getItem(position).getName();
        ((TextView) view.findViewById(android.R.id.text1)).setText(cityName);
        return view;
      }
    };
    setListAdapter(citiesAdapter);

    regionService = App.tripKit().getRegionService();
    regionService
        .getCitiesAsync()
        .toList()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<Location>>() {
          @Override public void call(List<Location> cities) {
            citiesAdapter.clear();
            citiesAdapter.addAll(cities);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable error) {
            Toast.makeText(getApplicationContext(), "Error loading cities", Toast.LENGTH_SHORT).show();
          }
        });
  }

  @Override protected void onListItemClick(ListView l, View v, int position, long id) {
    final Location city = citiesAdapter.getItem(position);
    regionService
        .getRegionByLocationAsync(city)
        .flatMap(new Func1<Region, Observable<Paratransit>>() {
          @Override public Observable<Paratransit> call(Region region) {
            return regionService.fetchParatransitByRegionAsync(region);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Paratransit>() {
          @Override public void call(Paratransit paratransit) {
            showParatransit(paratransit);
          }
        });
  }

  private void showParatransit(Paratransit paratransit) {
    new AlertDialog.Builder(this)
        .setMessage(String.format(
            Locale.US, "URL: %s\nname: %s\nnumber: %s",
            paratransit.url(),
            paratransit.name(),
            paratransit.number()
        ))
        .create()
        .show();
  }
}