package skedgo.tripkit.ui.timetable.services

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
/*import com.buzzhives.android.tripplanner.R
import com.buzzhives.android.tripplanner.activity.ServiceMapPagerActivity
import com.buzzhives.android.tripplanner.activity.ViewAlertsActivity
import com.buzzhives.android.tripplanner.databinding.FragmentViewTimetableBinding
import com.buzzhives.android.tripplanner.util.ErrorHandlers*/
import com.nineoldandroids.view.ViewPropertyAnimator.animate
import com.skedgo.android.common.model.RealtimeAlerts
import com.skedgo.android.common.model.ScheduledStop
//import com.skedgo.android.tripgo.event.TimeDatePickedEvent
//import com.skedgo.android.tripgo.fragment.TimeDatePickerFragment
/*import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector*/
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import skedgo.rxlifecyclecomponents.RxFragment
import skedgo.tripkit.android.DaggerTripKit
import skedgo.tripkit.ui.R
import skedgo.tripkit.ui.module.TripkitUIModule
import javax.inject.Inject

const val ARG_STOP = "stop"

class TimetableFragment2 : RxFragment() {

  companion object {
    fun newInstance(stop: ScheduledStop): TimetableFragment2 {
      val args = Bundle()
      args.putParcelable(ARG_STOP, stop)

      val fragment = TimetableFragment2()
      fragment.arguments = args
      return fragment
    }
  }

  //@Inject
 // internal lateinit var timetableViewModel: TimetableViewModel

  private var mRefreshMenuItem: MenuItem? = null

  private var binding: skedgo.tripkit.ui.databinding.FragmentViewTimetableBinding? = null


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)


   /* val bookingUiComponent = DaggerTripkitUiComponent.builder()
        .bookingUiModule(TripkitUIModule(activity!!.applicationContext))
        .build()
    DataBindingUtil.setDefaultComponent(bookingUiComponent)
    bookingUiComponent
        .inject(this)*/

    setHasOptionsMenu(true)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_view_timetable, container, false)

    binding = DataBindingUtil.bind(view)
   // binding!!.viewModel = timetableViewModel

    binding!!.timetableList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy >= 0) {
          binding!!.goToNowButton.visibility = View.GONE
        } else {
          binding!!.goToNowButton.visibility = View.VISIBLE
        }
      }

      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (!recyclerView.canScrollVertically(1)) {
         // timetableViewModel.downloadMoreTimetableAsync()
        }
      }
    })
/*
    timetableViewModel.onAlertClicks
        .observeOn(AndroidSchedulers.mainThread())
        // .takeUntil(lifecycle().onDestroy())
        .subscribe { alerts ->
          //startActivity(
              //    ViewAlertsActivity.newIntent(activity!!, alerts))
        }

    timetableViewModel.onServiceClick
        .observeOn(AndroidSchedulers.mainThread())
        //  .takeUntil(lifecycle().onDestroy())
        .subscribe { (first, second) ->
          /*startActivity(ServiceMapPagerActivity.newIntent(
              activity,
              second.serviceTripId,
              first))*/
        }

    timetableViewModel.onAlertForIdAdded()
        .observeOn(AndroidSchedulers.mainThread())
        // .takeUntil(lifecycle().onDestroy())
        .subscribe({ alert ->
       //   binding!!.serviceAlertView.setTitle(alert.title())
       //   binding!!.serviceAlertView.setText(RealtimeAlerts.getDisplayText(alert))
       //   binding!!.serviceAlertView.show()
        }, { Log.e("TimetableFragment2", "Error $it") })
*/
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val stop = arguments!!.getParcelable<ScheduledStop>(ARG_STOP)
    activity!!.invalidateOptionsMenu()
    downloadTimetableAsync()

    presentServiceFilterView()

   // animate(binding!!.serviceAlertView).alpha(0.0f)
    presentGoToNowButton()

   // timetableViewModel.stop.call(stop)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    presentActionBarText()
  }

  override fun onDestroy() {
    super.onDestroy()
   // timetableViewModel.onCleared()
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    inflater!!.inflate(R.menu.menu_stop_timetable2, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onPrepareOptionsMenu(menu: Menu?) {
    Log.e("**", "onPrepareOptionsMenu")

    mRefreshMenuItem = menu!!.findItem(R.id.refresh)

    val shareTimetable = menu.findItem(R.id.shareQuery)

   // shareTimetable.isVisible = timetableViewModel.services.get() != null && !timetableViewModel.services.get()!!.isEmpty()
  //  shareTimetable.isEnabled = timetableViewModel.services.get() != null && !timetableViewModel.services.get()!!.isEmpty()

    super.onPrepareOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    val i = item!!.itemId
    if (i == R.id.refresh) {
      refresh()

    } else if (i == R.id.shareQuery) {
    /*  timetableViewModel.getShareUrl()
          .subscribe { url: String ->
            val intentPartager = Intent(Intent.ACTION_SEND)
            intentPartager.type = "text/plain"
            intentPartager.putExtra(Intent.EXTRA_TEXT, url)
            val startingIntent = Intent.createChooser(intentPartager, "Share this using...")
            startActivity(startingIntent)
          }*/
    } else if (i == R.id.setTimeMenuItem) {
      /*val datePicker = TimeDatePickerFragment
          .newInstance(getString(R.string.set_time))

      datePicker.setListener { this.onTimeDatePickerEvent(it) }

      datePicker.show(fragmentManager!!, null)*/
    }
    return false
  }

 /* private fun onTimeDatePickerEvent(event: TimeDatePickedEvent) {
    val mTimeInMillis = event.time.toMillis(false)
    timetableViewModel.onDateChanged.call(mTimeInMillis)
    downloadTimetableAsync(mTimeInMillis)
  }*/

  private fun presentGoToNowButton() {
 //   binding!!.goToNowButton.setOnClickListener { v: View -> binding!!.timetableList.scrollToPosition(timetableViewModel.getFirstNowPosition()) }
  }

  private fun presentServiceFilterView() {
    binding!!.serviceFilterView.queryField.setHint(R.string.filter_by_service_name_coma_number_or_stop)
  //  binding!!.serviceFilterView.setOnQueryTextListener { newText:CharSequence -> timetableViewModel.filter.call(newText.toString()) }
  }

  private fun presentActionBarText() {
    val actionBar = (activity as AppCompatActivity).supportActionBar
    //actionBar!!.title = timetableViewModel.getActionBarTitle()
   // actionBar.subtitle = timetableViewModel.getActionBarSubTitle()
  }

  private fun refresh() {
    downloadTimetableAsync()
  }

  private fun downloadTimetableAsync() {
   // timetableViewModel.downloadTimetableAsync(null)
  }

  private fun downloadTimetableAsync(sinceTimeInMillis: Long) {
   // timetableViewModel.downloadTimetableAsync(sinceTimeInMillis)
  }
}