package com.skedgo.android.samples;

import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.TransportMode;
import com.skedgo.android.common.util.TransportModeUtils;
import com.squareup.picasso.Picasso;

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
            .inflate(R.layout.view_transport_mode, parent, false);

        final TransportMode mode = getItem(position);
        final String title = mode.getTitle();
        ((TextView) view.findViewById(R.id.textView)).setText(title);

        final String iconUrl = TransportModeUtils.getIconUrlForTransportMode(getResources(), mode);
        final Uri iconUri = iconUrl == null ? Uri.EMPTY : Uri.parse(iconUrl);
        Picasso.with(getContext().getApplicationContext())
            .load(iconUri)
            .placeholder(R.drawable.ic_car_ride_share)
            .error(R.drawable.ic_car_ride_share)
            .into(((ImageView) view.findViewById(R.id.imageView)));

        return view;
      }
    };
    setListAdapter(transportModesAdapter);

    App.tripKit()
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