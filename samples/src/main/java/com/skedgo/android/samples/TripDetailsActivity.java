package com.skedgo.android.samples;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.util.TripSegmentUtils;

public class TripDetailsActivity extends AppCompatActivity {
  private static final String TRIP_EXTRA = "TripExtra";

  public static Intent newIntent(Context context, Trip trip) {
    Intent intent = new Intent(context, TripDetailsActivity.class);
    intent.putExtra(TRIP_EXTRA, trip);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trip_details);
    final Trip trip = getIntent().getParcelableExtra(TRIP_EXTRA);
    RecyclerView segmentList = (RecyclerView) findViewById(R.id.segmentList);
    segmentList.setLayoutManager(new LinearLayoutManager(this));
    segmentList.setAdapter(new RecyclerView.Adapter<SegmentViewHolder>() {

      @Override public SegmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SegmentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.segment, parent, false));
      }

      @Override public void onBindViewHolder(SegmentViewHolder holder, int position) {
        holder.actionTitle.setText(TripSegmentUtils.getTripSegmentAction(holder.itemView.getContext(), trip.getSegments().get(position)));
      }

      @Override public int getItemCount() {
        return trip.getSegments().size();
      }
    });
    BottomSheetBehavior<View> from = BottomSheetBehavior.from(findViewById(R.id.bottomSheet));
  }

  private static class SegmentViewHolder extends RecyclerView.ViewHolder {
    final TextView actionTitle;

    SegmentViewHolder(View itemView) {
      super(itemView);
      actionTitle = (TextView) itemView.findViewById(R.id.segmentTitle);
    }
  }
}
