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

        // Show mode title.
        final String title = mode.getTitle();
        ((TextView) view.findViewById(R.id.textView)).setText(title);

        // Show light remote icon.
        final String lightIconUrl = TransportModeUtils.getIconUrlForTransportMode(getResources(), mode);
        final Uri lightIconUri = lightIconUrl == null ? Uri.EMPTY : Uri.parse(lightIconUrl);
        Picasso.with(getContext().getApplicationContext())
            .load(lightIconUri)
            .placeholder(R.drawable.ic_car_ride_share)
            .error(R.drawable.ic_car_ride_share)
            .into(((ImageView) view.findViewById(R.id.lightImageView)));

        // Show dark remote icon.
        final String darkIconUrl = TransportModeUtils.getDarkIconUrlForTransportMode(getResources(), mode);
        final Uri darkIconUri = darkIconUrl == null ? Uri.EMPTY : Uri.parse(darkIconUrl);
        Picasso.with(getContext().getApplicationContext())
            .load(darkIconUri)
            .into(((ImageView) view.findViewById(R.id.darkImageView)));

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