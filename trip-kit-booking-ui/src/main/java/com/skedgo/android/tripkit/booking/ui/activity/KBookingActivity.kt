package com.skedgo.android.tripkit.booking.ui.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.widget.Toast
import com.skedgo.android.tripkit.booking.ui.BR
import com.skedgo.android.tripkit.booking.ui.BookingUiModule
import com.skedgo.android.tripkit.booking.ui.DaggerBookingUiComponent
import com.skedgo.android.tripkit.booking.ui.R
import com.skedgo.android.tripkit.booking.ui.databinding.FragmentKbookingBinding
import com.skedgo.android.tripkit.booking.ui.viewmodel.BookingFormFieldViewModel
import com.skedgo.android.tripkit.booking.ui.viewmodel.FieldPasswordViewModel
import com.skedgo.android.tripkit.booking.ui.viewmodel.FieldStringViewModel
import com.skedgo.android.tripkit.booking.ui.viewmodel.KBookingFormViewModel
import me.tatarka.bindingcollectionadapter.ItemViewSelector
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector
import rx.android.schedulers.AndroidSchedulers.mainThread
import skedgo.activityanimations.AnimatedTransitionActivity
import skedgo.rxlifecyclecomponents.bindToLifecycle
import javax.inject.Inject

const val KEY_URL = "url"
const val KEY_FORM = "form"

open class KBookingActivity : AnimatedTransitionActivity() {

  companion object {
    @JvmStatic fun bookingFormsView(): ItemViewSelector<Any> {
      return ItemViewClassSelector.builder<Any>()
          .put(FieldStringViewModel::class.java, BR.viewModel, R.layout.field_string)
          .put(FieldPasswordViewModel::class.java, BR.viewModel, R.layout.field_password)
          .put(BookingFormFieldViewModel::class.java, BR.viewModel, R.layout.field_booking_form)
          .put(String::class.java, BR.title, R.layout.group_title)
          .build()
    }
  }

  @Inject lateinit var viewModel: KBookingFormViewModel
  val binding: FragmentKbookingBinding by lazy {
    DataBindingUtil.setContentView<FragmentKbookingBinding>(this, R.layout.fragment_kbooking)
  }

  private val onError =
      {
        error: Throwable ->
        Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
        finish()
      }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    DaggerBookingUiComponent.builder()
        .bookingUiModule(BookingUiModule(applicationContext))
        .build()
        .inject(this)

    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    binding.setViewModel(viewModel)

    viewModel.onUpdateFormTitle
        .asObservable()
        .observeOn(mainThread())
        .bindToLifecycle(this)
        .subscribe({ title ->
          supportActionBar?.title = title
        }, onError)

    viewModel.onNextBookingForm
        .asObservable()
        .observeOn(mainThread())
        .bindToLifecycle(this)
        .subscribe({ bookingForm ->
          intent = Intent(this, this.javaClass)
          intent.putExtra(KEY_FORM, bookingForm as Parcelable)
          startActivity(intent)
        }, onError)

    viewModel.fetchBookingFormAsync(intent.extras)
        .bindToLifecycle(this)
        .subscribe({}, onError)

  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      onBackPressed()
      return true
    } else {
      return super.onOptionsItemSelected(item)
    }
  }


  open fun reportProblem() {}

  override fun onDestroy() {
    super.onDestroy()
    viewModel.dispose()
  }

}