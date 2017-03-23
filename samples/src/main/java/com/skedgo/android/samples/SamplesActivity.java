package com.skedgo.android.samples;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SamplesActivity extends ListActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    setListAdapter(new ArrayAdapter<>(
        this,
        android.R.layout.simple_list_item_1,
        new String[] {"Cities", "Transport in Los Angeles, CA, USA", "Routes"}
    ));
  }

  @Override protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    if (position == 0) {
      startActivity(new Intent(this, CitiesActivity.class));
    } else if (position == 1) {
      startActivity(new Intent(this, TransportModesActivity.class));
    } else if (position == 2) {
      startActivity(new Intent(this, RoutesActivity.class));
    }
  }
}