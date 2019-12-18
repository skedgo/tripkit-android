package com.skedgo.tripkit.booking.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.MenuItem
import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.ui.BR
import com.skedgo.tripkit.booking.ui.BookingUiModule
import com.skedgo.tripkit.booking.ui.DaggerBookingUiComponent
import com.skedgo.tripkit.booking.ui.R
import com.skedgo.tripkit.booking.ui.databinding.ActivityBookingBinding
import com.skedgo.tripkit.booking.ui.viewmodel.*
import com.skedgo.rxlifecyclecomponents.RxAppCompatActivity
import com.skedgo.rxlifecyclecomponents.bindToLifecycle
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import javax.inject.Inject

const val BOOKING_TYPE_URL = 0
const val BOOKING_TYPE_FORM = BOOKING_TYPE_URL + 1
const val BOOKING_TYPE_ACTION = BOOKING_TYPE_FORM + 1

const val KEY_TYPE = "type"
const val KEY_URL = "url"
const val KEY_FORM = "form"

const val EXTRA_DONE = "done"
const val EXTRA_CANCEL = "cancel"

const val RQ_BOOKING = 0
const val RQ_EXTERNAL = RQ_BOOKING + 1
const val RQ_EXTERNAL_WEB = RQ_EXTERNAL + 1

open class BookingActivity : RxAppCompatActivity() {
  companion object {
    @JvmStatic
    fun bookingFormsView(): ItemBinding<Any> {
      return ItemBinding.of(
          OnItemBindClass<Any>()
              .map(FieldStringViewModel::class.java, BR.viewModel, R.layout.field_string)
              .map(FieldPasswordViewModel::class.java, BR.viewModel, R.layout.field_password)
              .map(FieldExternalViewModel::class.java, BR.viewModel, R.layout.field_external)
              .map(FieldBookingFormViewModel::class.java, BR.viewModel, R.layout.field_booking_form)
              .map(String::class.java, BR.title, R.layout.group_title)
      )
    }
  }

  @Inject
  lateinit var viewModel: BookingFormViewModel
  val binding: ActivityBookingBinding by lazy {
    DataBindingUtil.setContentView<ActivityBookingBinding>(this, R.layout.activity_booking)
  }

  private val onError: (Throwable) -> Unit = { error: Throwable ->
    Log.e("BookingActivity", "Error on booking", error)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val bookingUiComponent = DaggerBookingUiComponent.builder()
        .bookingUiModule(BookingUiModule(applicationContext))
        .build()
    DataBindingUtil.setDefaultComponent(bookingUiComponent)
    bookingUiComponent
        .inject(this)

    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    binding.viewModel = viewModel

    viewModel.onUpdateFormTitle
        .observeOn(mainThread())
        .bindToLifecycle(this)
        .subscribe({ title ->
          supportActionBar?.title = title
        }, onError)

    viewModel.onDone
        .observeOn(mainThread())
        .bindToLifecycle(this)
        .subscribe({ form ->
          finishWithDone(form)
        }, onError)

    viewModel.onCancel
        .observeOn(mainThread())
        .bindToLifecycle(this)
        .subscribe({
          finishWithCancel()
        }, onError)

    viewModel.onErrorRetry
        .observeOn(mainThread())
        .bindToLifecycle(this)
        .subscribe({ url ->
          finishWithRetry(url)
        }, onError)

    viewModel.onNextBookingForm
        .observeOn(mainThread())
        .bindToLifecycle(this)
        .subscribe({ bookingForm ->
          intent = Intent(this, this.javaClass)
          intent.putExtra(KEY_TYPE, BOOKING_TYPE_FORM)
          intent.putExtra(KEY_FORM, bookingForm as Parcelable)
          startActivityForResult(intent, RQ_BOOKING)
        }, onError)

    viewModel.onNextBookingFormAction
        .observeOn(mainThread())
        .bindToLifecycle(this)
        .subscribe({ bookingForm ->
          intent = Intent(this, this.javaClass)
          intent.putExtra(KEY_TYPE, BOOKING_TYPE_ACTION)
          intent.putExtra(KEY_FORM, bookingForm as Parcelable)
          startActivityForResult(intent, RQ_BOOKING)
        }, onError)


    viewModel.onExternalForm
        .observeOn(mainThread())
        .bindToLifecycle(this)
        .subscribe({ externalFormField ->
          val intent = ExternalWebActivity.newIntent(this, externalFormField)
          startActivityForResult(intent, RQ_EXTERNAL_WEB)
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

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    when (resultCode) {
      Activity.RESULT_OK ->
        when {
          requestCode == RQ_EXTERNAL_WEB -> goNextUrl(data?.getStringExtra(KEY_URL))
          data?.getBooleanExtra(EXTRA_DONE, false) ?: false ||
              data?.getBooleanExtra(EXTRA_CANCEL, false) ?: false -> finishOkWithSameData(data!!)
        }
    }
  }

  open fun reportProblem() {}

  override fun onDestroy() {
    super.onDestroy()
    viewModel.dispose()
  }

  private fun goNextUrl(url: String?) {
    intent = Intent(this, this.javaClass)
    intent.putExtra(KEY_TYPE, BOOKING_TYPE_URL)
    intent.putExtra(KEY_URL, url)
    startActivityForResult(intent, RQ_BOOKING)
  }

  private fun finishWithDone(form: BookingForm?) {
    val done = Intent()
    done.putExtra(EXTRA_DONE, true)
    if (form != null) {
      done.putExtra(KEY_FORM, form as Parcelable)
    }
    setResult(Activity.RESULT_OK, done)
    finish()
  }

  private fun finishWithCancel() {
    val cancel = Intent()
    cancel.putExtra(EXTRA_CANCEL, true)
    setResult(Activity.RESULT_OK, cancel)
    finish()
  }

  private fun finishOkWithSameData(data: Intent) {
    setResult(Activity.RESULT_OK, data)
    finish()
  }

  private fun finishWithRetry(nextUrl: String) {
    val intent = Intent(this, this.javaClass)
    intent.putExtra(KEY_URL, nextUrl)
    startActivityForResult(intent, RQ_BOOKING)
    finish()
  }
}
