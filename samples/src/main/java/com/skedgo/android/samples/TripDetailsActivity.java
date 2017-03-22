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

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.util.TripSegmentUtils;
import com.skedgo.android.tripkit.RouteService;
import com.skedgo.android.tripkit.TripKit;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

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
    final RecyclerView segmentList = (RecyclerView) findViewById(R.id.segmentList);

    // final Trip trip = getIntent().getParcelableExtra(TRIP_EXTRA);
    Query query = new Query();
    query.setFromLocation(new Location(10.810491, 106.705726));
    query.setToLocation(new Location(10.754426, 106.624733));
    TripKit.singleton().getRouteService()
        .routeAsync(query)
        .flatMap(new Func1<List<TripGroup>, Observable<TripGroup>>() {
          @Override public Observable<TripGroup> call(List<TripGroup> tripGroups) {
            return Observable.from(tripGroups);
          }
        })
        .map(new Func1<TripGroup, Trip>() {
          @Override public Trip call(TripGroup tripGroup) {
            return tripGroup.getDisplayTrip();
          }
        })
        .toSortedList(new Func2<Trip, Trip, Integer>() {
          @Override public Integer call(Trip trip, Trip trip2) {
            return trip.getSegments().size() - trip2.getSegments().size();
          }
        })
        .map(new Func1<List<Trip>, Trip>() {
          @Override public Trip call(List<Trip> trips) {
            return trips.get(trips.size() - 1);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Trip>() {
          @Override public void call(final Trip trip) {
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
          }
        });
    segmentList.setLayoutManager(new LinearLayoutManager(this));
    BottomSheetBehavior<View> from = BottomSheetBehavior.from(findViewById(R.id.bottomSheet));
    from.setPeekHeight(360);
    // from.setState();
  }

  private static class SegmentViewHolder extends RecyclerView.ViewHolder {
    final TextView actionTitle;

    SegmentViewHolder(View itemView) {
      super(itemView);
      actionTitle = (TextView) itemView.findViewById(R.id.segmentTitle);
    }
  }
}
