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
import com.skedgo.android.common.model.TransportMode;
import com.skedgo.android.tripkit.TripKit;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class TransportModesActivity extends ListActivity {
  private ArrayAdapter<TransportMode> transportModesAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    transportModesAdapter = new ArrayAdapter<TransportMode>(this, android.R.layout.simple_list_item_1) {
      @Override public View getView(int position, View convertView, ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext())
            .inflate(android.R.layout.simple_list_item_1, parent, false);
        final String title = getItem(position).getTitle();
        ((TextView) view.findViewById(android.R.id.text1)).setText(title);
        return view;
      }
    };
    setListAdapter(transportModesAdapter);

    TripKit.with(getApplicationContext())
        .getRegionService()
        .getTransportModesByLocationAsync(new Location(34.044, -118.248))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<TransportMode>>() {
          @Override public void call(List<TransportMode> modes) {
            transportModesAdapter.clear();
            transportModesAdapter.addAll(modes);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable error) {
            Toast.makeText(getApplicationContext(), "Error loading transport modes", Toast.LENGTH_SHORT).show();
          }
        });
  }
}