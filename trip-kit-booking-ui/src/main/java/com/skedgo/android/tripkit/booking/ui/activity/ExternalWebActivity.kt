package com.skedgo.android.tripkit.booking.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import com.skedgo.android.tripkit.booking.ExternalFormField
import com.skedgo.android.tripkit.booking.ui.BookingUiModule
import com.skedgo.android.tripkit.booking.ui.DaggerBookingUiComponent
import com.skedgo.android.tripkit.booking.ui.R
import com.skedgo.android.tripkit.booking.ui.databinding.ExternalWebBinding
import com.skedgo.android.tripkit.booking.ui.viewmodel.ExternalViewModel
import rx.android.schedulers.AndroidSchedulers
import skedgo.rxlifecyclecomponents.RxAppCompatActivity
import java.io.Serializable
import javax.inject.Inject

const val KEY_EXTERNAL_FORM = "externalFormField"

class ExternalWebActivity : RxAppCompatActivity() {

  companion object {

    @JvmStatic fun newIntent(
        context: Context,
        externalFormField: ExternalFormField): Intent {
      return Intent(context, ExternalWebActivity::class.java)
          .putExtra(KEY_EXTERNAL_FORM, externalFormField as Serializable)
    }
  }

  @Inject lateinit var viewModel: ExternalViewModel
  val binding: ExternalWebBinding by lazy {
    DataBindingUtil.setContentView<ExternalWebBinding>(this, R.layout.external_web)
  }

  public override fun onDestroy() {
    viewModel.dispose()
    super.onDestroy()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    DaggerBookingUiComponent.builder()
        .bookingUiModule(BookingUiModule(applicationContext))
        .build()
        .inject(this)

    binding.setViewModel(viewModel)

    val args = intent.extras
    viewModel.handleArgs(args)

    binding.webView.settings.javaScriptEnabled = true
    binding.webView.setWebViewClient(viewModel.webViewClient())

    viewModel.nextUrlObservable
        .observeOn(AndroidSchedulers.mainThread())
        .compose(this.bindToLifecycle<String>())
        .subscribe({ nextUrl ->
          val data = Intent()
          data.putExtra(KEY_URL, nextUrl)
          setResult(Activity.RESULT_OK, data)
          finish()
        }) { error ->
          Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
          finish()
        }
  }

}