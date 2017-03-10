package com.skedgo.android.tripkit.booking.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import com.skedgo.android.tripkit.booking.BookingForm
import com.skedgo.android.tripkit.booking.ExternalFormField
import com.skedgo.android.tripkit.booking.ui.BookingUiModule
import com.skedgo.android.tripkit.booking.ui.DaggerBookingUiComponent
import com.skedgo.android.tripkit.booking.ui.OAuth2CallbackHandler
import com.skedgo.android.tripkit.booking.ui.R
import com.skedgo.android.tripkit.booking.ui.databinding.ExternalProviderAuthBinding
import com.skedgo.android.tripkit.booking.ui.databinding.ExternalWebBinding
import com.skedgo.android.tripkit.booking.ui.viewmodel.ExternalProviderAuthViewModel
import com.skedgo.android.tripkit.booking.ui.viewmodel.ExternalViewModel
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import skedgo.rxlifecyclecomponents.RxAppCompatActivity
import java.io.Serializable
import javax.inject.Inject

class ExternalProviderAuthActivity : RxAppCompatActivity() {

  companion object {

    @JvmStatic fun newIntent(context: Context, bookingForm: BookingForm): Intent {
      val intent = Intent(context, ExternalProviderAuthActivity::class.java)
      intent.putExtra(KEY_FORM, bookingForm as Parcelable)
      return intent
    }
  }


  @Inject lateinit var viewModel: ExternalProviderAuthViewModel
  @Inject lateinit var oAuth2CallbackHandler: OAuth2CallbackHandler
  val binding: ExternalProviderAuthBinding by lazy {
    DataBindingUtil.setContentView<ExternalProviderAuthBinding>(this, R.layout.external_provider_auth)
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

    // hack to solve web view keyboard issue
    binding.edit.isFocusable = true
    binding.edit.requestFocus()

    binding.webView.settings.javaScriptEnabled = true
    binding.webView.setWebViewClient(viewModel.webViewClient(oAuth2CallbackHandler))

    viewModel.intentObservable
        .observeOn(AndroidSchedulers.mainThread())
        .compose(this.bindToLifecycle<Intent>())
        .subscribe({ intent ->
          setResult(Activity.RESULT_OK, intent)
          finish()
        }) { error ->
          Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
          finish()
        }
  }


}