package com.skedgo.android.tripkit.booking.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import com.skedgo.android.tripkit.booking.ui.BookingUiModule
import com.skedgo.android.tripkit.booking.ui.DaggerBookingUiComponent
import com.skedgo.android.tripkit.booking.ui.R
import com.skedgo.android.tripkit.booking.ui.activity.BookingActivity.KEY_URL
import com.skedgo.android.tripkit.booking.ui.databinding.FragmentKbookingBinding
import com.skedgo.android.tripkit.booking.ui.viewmodel.KBookingFormViewModel
import skedgo.activityanimations.AnimatedTransitionActivity
import skedgo.rxlifecyclecomponents.bindToLifecycle
import javax.inject.Inject


open class KBookingActivity : AnimatedTransitionActivity() {

  @Inject lateinit var viewModel: KBookingFormViewModel
  val binding: FragmentKbookingBinding by lazy {
    DataBindingUtil.setContentView<FragmentKbookingBinding>(this, R.layout.fragment_kbooking)
  }

  override protected fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    DaggerBookingUiComponent.builder()
        .bookingUiModule(BookingUiModule(applicationContext))
        .build()
        .inject(this)

    val actionBar = supportActionBar
    if (actionBar != null) {
      actionBar.setDisplayShowHomeEnabled(true)
      actionBar.setDisplayHomeAsUpEnabled(true)
    }

    binding.setFormViewModel(viewModel)

    val url = intent.getStringExtra(KEY_URL)

    viewModel.fetchBookingFormAsync(url)
        .bindToLifecycle(this)
        .subscribe({}, { error ->
          Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
          finish()
        })

  }

  open fun reportProblem() {}

  override fun onDestroy() {
    super.onDestroy()
    viewModel.dispose()
  }

}