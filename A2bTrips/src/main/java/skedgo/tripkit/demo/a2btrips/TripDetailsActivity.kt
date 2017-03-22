package skedgo.tripkit.demo.a2btrips

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.Query
import com.skedgo.android.common.model.Trip
import com.skedgo.android.common.util.TripSegmentUtils
import com.skedgo.android.tripkit.TripKit
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import rx.functions.Func2

class TripDetailsActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_trip_details)
    val segmentList = findViewById(R.id.segmentList) as RecyclerView
    // final Trip trip = getIntent().getParcelableExtra(TRIP_EXTRA);
    val query = Query()
    query.setFromLocation(Location(10.810491, 106.705726))
    query.setToLocation(Location(10.754426, 106.624733))
    TripKit.singleton().routeService
        .routeAsync(query)
        .flatMap { tripGroups -> Observable.from(tripGroups) }
        .map { tripGroup -> tripGroup.displayTrip }
        .toSortedList{ trip, trip2 -> trip!!.segments.size - trip2!!.segments.size }
        .map { trips -> trips[trips.size - 1] }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe  { trip ->
          segmentList.adapter = object : RecyclerView.Adapter<SegmentViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SegmentViewHolder {
              return SegmentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.segment, parent, false))
            }

            override fun onBindViewHolder(holder: SegmentViewHolder, position: Int) {
              holder.actionTitle.text = TripSegmentUtils.getTripSegmentAction(holder.itemView.context, trip!!.segments[position])
            }

            override fun getItemCount(): Int {
              return trip!!.segments.size
            }
          }
        }
    segmentList.layoutManager = LinearLayoutManager(this)
    val from = BottomSheetBehavior.from(findViewById(R.id.bottomSheet))
    from.peekHeight = 360
    // from.setState();
  }

  private class SegmentViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal val actionTitle: TextView = itemView.findViewById(R.id.segmentTitle) as TextView

  }

  companion object {
    private val TRIP_EXTRA = "TripExtra"

    fun newIntent(context: Context, trip: Trip): Intent {
      val intent = Intent(context, TripDetailsActivity::class.java)
      intent.putExtra(TRIP_EXTRA, trip)
      return intent
    }
  }
}
