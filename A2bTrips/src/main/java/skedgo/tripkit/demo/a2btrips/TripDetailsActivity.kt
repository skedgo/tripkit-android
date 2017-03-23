package skedgo.tripkit.demo.a2btrips

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.skedgo.android.common.model.Trip
import com.skedgo.android.common.util.TripSegmentUtils
import com.skedgo.android.tripkit.TripLinesProcessor
import rx.Observable
import skedgo.tripkit.demo.a2btrips.databinding.ActivityTripDetailsBinding

const val TRIP_JSON = "{\"caloriesCost\":48.0,\"currencySymbol\":\"đ\",\"carbonCost\":0.1,\"arrive\":1490250410,\"hassleCost\":9.6,\"mId\":90,\"mIsFavourite\":false,\"moneyCost\":18000.0,\"mSegments\":[{\"durationWithoutTraffic\":0,\"realTime\":false,\"action\":\"Leave Chu Văn An \\u0026 Đường số 8 at \\u003cTIME\\u003e\",\"travelDirection\":0,\"endTime\":1490245863,\"frequency\":0,\"from\":{\"address\":\"Chu Văn An \\u0026 Đường số 8\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.81049,\"class\":\"Location\",\"lng\":106.70573,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"mId\":1,\"isContinuation\":false,\"serviceIsFrequencyBased\":false,\"startTime\":1490245863,\"to\":{\"address\":\"Chu Văn An \\u0026 Đường số 8\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.81049,\"class\":\"Location\",\"lng\":106.70573,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"type\":\"DEPARTURE\",\"visibility\":\"in details\",\"metres\":0,\"metresSafe\":0,\"stops\":0,\"segmentTemplateHashCode\":0,\"wheelchairAccessible\":false},{\"durationWithoutTraffic\":0,\"realTime\":false,\"action\":\"\\u003cTIME\\u003e: Walk\",\"travelDirection\":89,\"endTime\":1490246520,\"frequency\":0,\"from\":{\"address\":\"Chu Văn An \\u0026 Đường số 8\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.81049,\"class\":\"Location\",\"lng\":106.70573,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"mId\":2,\"isContinuation\":false,\"serviceIsFrequencyBased\":false,\"notes\":\"\\u003cDURATION\\u003e ⋅ 550m\",\"startTime\":1490245863,\"streets\":[{\"encodedWaypoints\":\"ql~`Aw}gjS}A_@VeANsA@u@NmCAIF}DFmB??tA?fAC?P\",\"meters\":0.0,\"safe\":false}],\"to\":{\"address\":\"Ngã Tư Chu Văn An\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.80981,\"class\":\"Location\",\"lng\":106.70912,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"type\":\"UNSCHEDULED\",\"visibility\":\"in summary\",\"metres\":515,\"metresSafe\":0,\"modeInfo\":{\"alt\":\"Walk\",\"identifier\":\"wa_wal\",\"localIcon\":\"walk\"},\"stops\":0,\"segmentTemplateHashCode\":-1564385377,\"modeIdentifier\":\"wa_wal\",\"wheelchairAccessible\":false},{\"durationWithoutTraffic\":0,\"realTime\":false,\"action\":\"\\u003cTIME\\u003e: Take 08\",\"travelDirection\":221,\"endStopCode\":\"8556\",\"endTime\":1490248500,\"frequency\":0,\"from\":{\"address\":\"Ngã Tư Chu Văn An\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.80981,\"class\":\"Location\",\"lng\":106.70912,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"mId\":3,\"isContinuation\":false,\"serviceIsFrequencyBased\":false,\"notes\":\"Đại học Quốc Gia - Bến xe Quận 8\\nDirection: Bến xe Quận 8\\n\\u003cPLATFORM\\u003e\\n\\u003cDURATION\\u003e ⋅ \\u003cSTOPS\\u003e ⋅ SaiGonBus Ticket ($6000.00)\",\"serviceColor\":{\"blue\":46,\"green\":184,\"red\":46},\"serviceDirection\":\"Bến xe Quận 8\",\"serviceName\":\"Đại học Quốc Gia - Bến xe Quận 8\",\"serviceNumber\":\"08\",\"serviceOperator\":\"SaiGonBus\",\"serviceTripID\":\"1329\",\"shapes\":[{\"encodedWaypoints\":\"iabaA}bkjSHC`AxBd@nAn@nCd@jE??DQDLr@zEf@hCd@dBHnA??HCXxA|@hHCp@l@AdBNAPB\\\\LPNFPBd@E\\\\WbB@xAZ`AXf@HlJ|@`UbBfCPr@HVRRPn@`Af@d@Rd@^n@n@fA??FGVP|Av@nEhBtIfDdCfA??BKXHh@JdC@hEAxCB\",\"id\":0,\"travelled\":false},{\"encodedWaypoints\":\"ih~`A}rhjS?O|IM|DSh@C??TEB`@Ca@bDI|AGhBFlBF??J?tGLUrG??BQAXaA~W??@CE`BYfCw@rEC\\\\Fj@T|@??@@dCpI??JRx@~Bh@`CEtAc@pHGl@???F{@lL??ARSnE]dE??@Ao@`Ji@bJG`A??@?k@xH????Cn@@`@B\\\\Vx@d@l@f@j@tD`E??DClI`J??DE`C|BtDtDHd@@v@C~@????QlGYdH_@bKw@|T_@~H??B@g@~KeBpDo@h@??JFiAlB?PM`@mE`I??HDeApBh@ZjD|Cj@bA??JIdApA\\\\l@L^\\\\fBFRNJPJX@LAf@X\\\\x@??DA|BbCdAhAjDlDf@f@lFlG??FGdFnFkCtB??FDcBfBdCxC??FEbHxH??DGdBhBRJd@eA~BuFbD`B??BMdBn@l@NrATZFfEm@|C[??CMzLcCzBM??COrJcB`ImA??CMbRqDtAG??CMtHsAnF_AlFu@??CK~WwE???GzDy@vCY??EK`Es@bCc@dKqBx@???CKjB_@dHqAfDe@zBg@~@Q|@A??CKnHcB`Ek@|Cm@hCa@rFgAfDi@??AC~@Ud@`G\",\"id\":0,\"travelled\":true},{\"encodedWaypoints\":\"_ss`Auf_jSJAj@dH??FAj@hF`@J\\\\FnBWdDaAdB???CMxAMd@BvAF???UjBMIQDSLKN?LJb@yAR_@TI|Bk@pCo@|A[\\\\Ah@?tCRt@JJFDP?XB`@NPp@v@dAdAn@d@n@ZXbAfDfHb@xA??JE~@zB^d@FHVTj@Z`AVv@DlAOjA[`FyAj@Md@Kj@Q`@AhDRt@HN?XEl@Ad@C\",\"id\":0,\"travelled\":false}],\"stopCode\":\"7216\",\"startTime\":1490246520,\"to\":{\"address\":\"Bệnh viện Hùng Vương\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.75521,\"class\":\"Location\",\"lng\":106.66108,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"type\":\"SCHEDULED\",\"visibility\":\"in summary\",\"metres\":0,\"metresSafe\":0,\"modeInfo\":{\"alt\":\"Bus\",\"identifier\":\"pt_pub_bus\",\"localIcon\":\"bus\"},\"stops\":35,\"segmentTemplateHashCode\":-1315925550,\"modeIdentifier\":\"pt_pub\",\"wheelchairAccessible\":false},{\"durationWithoutTraffic\":0,\"realTime\":false,\"action\":\"\\u003cTIME\\u003e: Wait \",\"travelDirection\":0,\"endTime\":1490248620,\"frequency\":0,\"mId\":4,\"isContinuation\":false,\"serviceIsFrequencyBased\":false,\"notes\":\"\\u003cDURATION\\u003e\",\"location\":{\"address\":\"Bệnh viện Hùng Vương\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.75521,\"class\":\"Location\",\"lng\":106.66108,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"startTime\":1490248500,\"type\":\"STATIONARY\",\"visibility\":\"in details\",\"metres\":0,\"metresSafe\":0,\"modeInfo\":{\"alt\":\"Transfer\",\"localIcon\":\"transfer\"},\"stops\":0,\"segmentTemplateHashCode\":-853566958,\"wheelchairAccessible\":false},{\"durationWithoutTraffic\":0,\"realTime\":false,\"action\":\"\\u003cTIME\\u003e: Take 150\",\"travelDirection\":245,\"endStopCode\":\"9000\",\"endTime\":1490248800,\"frequency\":0,\"from\":{\"address\":\"Bệnh viện Hùng Vương\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.75521,\"class\":\"Location\",\"lng\":106.66108,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"mId\":5,\"isContinuation\":false,\"serviceIsFrequencyBased\":false,\"notes\":\"Ngã 3 Tân Vạn - Bến xe Chợ Lớn\\nDirection: Bến xe Chợ Lớn\\n\\u003cPLATFORM\\u003e\\n\\u003cDURATION\\u003e ⋅ \\u003cSTOPS\\u003e ⋅ SaiGonBus Ticket ($6000.00)\",\"serviceColor\":{\"blue\":46,\"green\":184,\"red\":46},\"serviceDirection\":\"Bến xe Chợ Lớn\",\"serviceName\":\"Ngã 3 Tân Vạn - Bến xe Chợ Lớn\",\"serviceNumber\":\"150\",\"serviceOperator\":\"SaiGonBus\",\"serviceTripID\":\"11458\",\"shapes\":[{\"encodedWaypoints\":\"weu`AifajSFKxFhDjFzD??FIfDrBCN?JFNNBLANKrDxC??HM|G`F??FMtExCz@dFLdACv@??LCtAnLb@bG\",\"id\":0,\"travelled\":false},{\"encodedWaypoints\":\"_ss`Auf_jSJAj@dH??FAb@vEL\\\\XrA??G@t@bGl@~E??G@|@|Ib@@\\\\FjC^`Cl@zANd@^d@jB\",\"id\":0,\"travelled\":true}],\"stopCode\":\"8556\",\"startTime\":1490248620,\"to\":{\"address\":\"Bến xe Chợ Lớn\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.75125,\"class\":\"Location\",\"lng\":106.65256,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"type\":\"SCHEDULED\",\"visibility\":\"in summary\",\"metres\":0,\"metresSafe\":0,\"modeInfo\":{\"alt\":\"Bus\",\"identifier\":\"pt_pub_bus\",\"localIcon\":\"bus\"},\"stops\":4,\"segmentTemplateHashCode\":-1589716398,\"modeIdentifier\":\"pt_pub\",\"wheelchairAccessible\":false},{\"durationWithoutTraffic\":0,\"realTime\":false,\"action\":\"\\u003cTIME\\u003e: Wait \",\"travelDirection\":0,\"endTime\":1490249040,\"frequency\":0,\"mId\":6,\"isContinuation\":false,\"serviceIsFrequencyBased\":false,\"notes\":\"\\u003cDURATION\\u003e\",\"location\":{\"address\":\"Bến xe Chợ Lớn\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.75125,\"class\":\"Location\",\"lng\":106.65256,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"startTime\":1490248800,\"type\":\"STATIONARY\",\"visibility\":\"in details\",\"metres\":0,\"metresSafe\":0,\"modeInfo\":{\"alt\":\"Transfer\",\"localIcon\":\"transfer\"},\"stops\":0,\"segmentTemplateHashCode\":-853607072,\"wheelchairAccessible\":false},{\"durationWithoutTraffic\":0,\"realTime\":false,\"action\":\"\\u003cTIME\\u003e: Take 81\",\"travelDirection\":281,\"endStopCode\":\"7449\",\"endTime\":1490249520,\"frequency\":0,\"from\":{\"address\":\"Bến xe Chợ Lớn\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.75125,\"class\":\"Location\",\"lng\":106.65256,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"mId\":7,\"isContinuation\":false,\"serviceIsFrequencyBased\":false,\"notes\":\"Bến xe Chợ Lớn - Lê Minh Xuân\\nDirection: Lê Minh Xuân\\n\\u003cPLATFORM\\u003e\\n\\u003cDURATION\\u003e ⋅ \\u003cSTOPS\\u003e ⋅ SaiGonBus Ticket ($6000.00)\",\"serviceColor\":{\"blue\":46,\"green\":184,\"red\":46},\"serviceDirection\":\"Lê Minh Xuân\",\"serviceName\":\"Bến xe Chợ Lớn - Lê Minh Xuân\",\"serviceNumber\":\"81\",\"serviceOperator\":\"SaiGonBus\",\"serviceTripID\":\"17738\",\"shapes\":[{\"encodedWaypoints\":\"izr`Aoq}iSv@rKsNm@c@fH??@Js@~M????u@hN???JSnDKTUVQFMLCN?JBN`@jABV?b@CbAG`BET??_@bIMzCGvA??@@CjA@|A?~AGdBCx@Hx@Jx@DV??@CN`@VXR^b@x@TZGFIHCJCXFXJFHHe@pDm@|F[|AY`CQxA]zDi@vEQtAkAxKw@tHE^Kb@c@`A[l@g@`Bi@dC]tAUr@Iv@Iv@?bAKj@\",\"id\":0,\"travelled\":true},{\"encodedWaypoints\":\"o_t`AoiwiSL??p@Cn@_@nDa@fD??H?KzBMvCAbAEtBMfBMX??H@o@rFWnBMnAGR???FM~A@nA`ArG@jC??NIAHAx@UbCKdAa@|E\",\"id\":0,\"travelled\":false}],\"stopCode\":\"9000\",\"startTime\":1490249040,\"to\":{\"address\":\"Siêu thị Phú Lâm\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.7572,\"class\":\"Location\",\"lng\":106.62057,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"type\":\"SCHEDULED\",\"visibility\":\"in summary\",\"metres\":0,\"metresSafe\":0,\"modeInfo\":{\"alt\":\"Bus\",\"identifier\":\"pt_pub_bus\",\"localIcon\":\"bus\"},\"stops\":7,\"segmentTemplateHashCode\":-286749317,\"modeIdentifier\":\"pt_pub\",\"wheelchairAccessible\":false},{\"durationWithoutTraffic\":0,\"realTime\":false,\"action\":\"\\u003cTIME\\u003e: Walk\",\"travelDirection\":107,\"endTime\":1490250410,\"frequency\":0,\"from\":{\"address\":\"Siêu thị Phú Lâm\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.7572,\"class\":\"Location\",\"lng\":106.62057,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"mId\":8,\"isContinuation\":false,\"serviceIsFrequencyBased\":false,\"notes\":\"\\u003cDURATION\\u003e ⋅ 700m\",\"startTime\":1490249520,\"streets\":[{\"encodedWaypoints\":\"o_t`AoiwiSHcABeAD}@d@aBvAcGr@gBn@qB??fBP`EFF?BsA\",\"meters\":0.0,\"safe\":false}],\"to\":{\"address\":\"An Dương Vương\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.75443,\"class\":\"Location\",\"lng\":106.62473,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"type\":\"UNSCHEDULED\",\"visibility\":\"in summary\",\"metres\":665,\"metresSafe\":0,\"modeInfo\":{\"alt\":\"Walk\",\"identifier\":\"wa_wal\",\"localIcon\":\"walk\"},\"stops\":0,\"segmentTemplateHashCode\":1791851012,\"modeIdentifier\":\"wa_wal\",\"wheelchairAccessible\":false},{\"durationWithoutTraffic\":0,\"realTime\":false,\"action\":\"Arrive at An Dương Vương at \\u003cTIME\\u003e\",\"travelDirection\":0,\"endTime\":1490250410,\"frequency\":0,\"from\":{\"address\":\"An Dương Vương\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.75443,\"class\":\"Location\",\"lng\":106.62473,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"mId\":9,\"isContinuation\":false,\"serviceIsFrequencyBased\":false,\"startTime\":1490250410,\"to\":{\"address\":\"An Dương Vương\",\"bearing\":2147483647,\"exact\":false,\"lastUpdatedTime\":0,\"lat\":10.75443,\"class\":\"Location\",\"lng\":106.62473,\"mAverageRating\":-1.0,\"mFavouriteSortOrderIndex\":0,\"mId\":0,\"mIsFavourite\":false,\"mLocationType\":-1,\"mRatingCount\":-1,\"popularity\":0,\"timezone\":\"Asia/Ho_Chi_Minh\"},\"type\":\"ARRIVAL\",\"visibility\":\"on map\",\"metres\":0,\"metresSafe\":0,\"stops\":0,\"segmentTemplateHashCode\":0,\"wheelchairAccessible\":false}],\"depart\":1490245863,\"plannedURL\":\"https://darkages.buzzhives.com/satapp/trip/planned/1a57f87d-fd66-4add-87dd-da47d5a90b1f\",\"queryIsLeaveAfter\":true,\"saveURL\":\"https://darkages.buzzhives.com/satapp/trip/save/1a57f87d-fd66-4add-87dd-da47d5a90b1f\",\"temporaryURL\":\"https://darkages.buzzhives.com/satapp/trip/1a57f87d-fd66-4add-87dd-da47d5a90b1f\",\"uuid\":\"f40c8942-56f6-4b3d-93d7-4c9b4c1fbf52\",\"weightedScore\":33.2}"

class TripDetailsActivity : AppCompatActivity() {

  val tripLinesProcessor = TripLinesProcessor()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = DataBindingUtil.setContentView<ActivityTripDetailsBinding>(this, R.layout.activity_trip_details)
    // final Trip trip = getIntent().getParcelableExtra(TRIP_EXTRA);
    val trip = Gson().fromJson(TRIP_JSON, Trip::class.java)
    val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
    mapFragment.getMapAsync { map ->
      // Add a marker in Sydney, Australia, and move the camera.
      tripLinesProcessor.getPolylineOptionsListAsync(trip.segments)
          .flatMap { Observable.from(it) }
          .subscribe { map.addPolyline(it) }

      map.animateCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.Builder()
          .include(LatLng(trip.from.lat, trip.from.lon))
          .include(LatLng(trip.to.lat, trip.to.lon))
          .build(), 200))
      map.addMarker(MarkerOptions()
          .position(LatLng(trip.from.lat, trip.from.lon))
          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
      map.addMarker(MarkerOptions()
          .position(LatLng(trip.to.lat, trip.to.lon))
          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
    }
    binding.segmentList.adapter = object : RecyclerView.Adapter<SegmentViewHolder>() {

      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SegmentViewHolder {
        return SegmentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.segment, parent, false))
      }

      override fun onBindViewHolder(holder: SegmentViewHolder, position: Int) {
        val tripSegment = trip.segments[position]
        holder.actionTitle.text = TripSegmentUtils.getTripSegmentAction(holder.itemView.context, tripSegment)
        holder.actionNotes.text = trip.segments[position].getDisplayNotes(holder.itemView.resources)
        if (!TextUtils.isEmpty(tripSegment.notes)) {
          holder.actionNotes.visibility = View.VISIBLE
        } else {
          holder.actionNotes.visibility = View.GONE
        }
      }

      override fun getItemCount(): Int {
        return trip.segments.size
      }
    }
    binding.segmentList.layoutManager = LinearLayoutManager(this)
    val from = BottomSheetBehavior.from(binding.bottomSheet)
    from.peekHeight = 360
    // from.setState();
  }

  private class SegmentViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    internal val actionTitle: TextView = itemView.findViewById(R.id.segmentActionTitle) as TextView
    internal val actionNotes: TextView = itemView.findViewById(R.id.segmentActionNotes) as TextView
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
