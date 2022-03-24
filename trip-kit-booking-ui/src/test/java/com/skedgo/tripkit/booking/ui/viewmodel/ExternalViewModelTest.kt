package com.skedgo.tripkit.booking.ui.viewmodel

import android.os.Bundle
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.booking.ExternalFormField
import com.skedgo.tripkit.booking.ui.activity.KEY_EXTERNAL_FORM
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExternalViewModelTest {

  private val viewModel: ExternalViewModel by lazy {
    ExternalViewModel()
  }

  @Test fun shouldSetArgs() {
    val args = Bundle()

    val externalFormField: ExternalFormField = mock()
    whenever(externalFormField.value).thenReturn("url")

    args.putParcelable(KEY_EXTERNAL_FORM, externalFormField)

    viewModel.handleArgs(args)

    assertThat(viewModel.externalFormField).isEqualTo(externalFormField)
    assertThat(viewModel.url.get()).isEqualTo("url")
  }

  @Test fun shouldNotSetArgsOnNull() {
    val args = Bundle()

    args.putParcelable(KEY_EXTERNAL_FORM, null)

    viewModel.handleArgs(args)

    assertThat(viewModel.externalFormField).isNull()
    assertThat(viewModel.url.get()).isNull()
  }

  @Test fun shouldHandleNoDisregardURL() {

    val externalFormField: ExternalFormField = mock()
    whenever(externalFormField.disregardURL).thenReturn("disregardURL")

    viewModel.externalFormField = externalFormField

    val shouldOverride = viewModel.handleCallback("url")

    assertThat(shouldOverride).isTrue()
    assertThat(viewModel.url.get()).isEqualTo("url")
    assertThat(viewModel.showWebView.get()).isTrue()
  }

  @Test fun shouldHandleNoCallback() {

    val externalFormField: ExternalFormField = mock()
    whenever(externalFormField.disregardURL).thenReturn("disregardURL")
    whenever(externalFormField.nextURL).thenReturn("nextURL")

    viewModel.externalFormField = externalFormField

    val subscriber =  viewModel.nextUrlObservable.test()

    val shouldOverride = viewModel.handleCallback("disregardURL")

    assertThat(shouldOverride).isFalse()
    subscriber.assertValue("nextURL")

  }
}