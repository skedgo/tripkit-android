package com.skedgo.android.tripkit.booking.ui.viewmodel

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.android.tripkit.booking.ExternalFormField
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import rx.observers.TestSubscriber
import rx.subjects.PublishSubject

@RunWith(RobolectricTestRunner::class)
class FieldExternalViewModelTest {

  @Test fun shouldSetTitle() {

    val externalFormField: ExternalFormField = mock()
    whenever(externalFormField.title).thenReturn("title")

    val viewModel: FieldExternalViewModel = FieldExternalViewModel(externalFormField, PublishSubject.create())

    assertThat(viewModel.title).isEqualTo("title")

  }

  @Test fun shouldSetEmptyTitle() {

    val externalFormField: ExternalFormField = mock()
    whenever(externalFormField.title).thenReturn(null)

    val viewModel: FieldExternalViewModel = FieldExternalViewModel(externalFormField, PublishSubject.create())

    assertThat(viewModel.title).isEqualTo("")

  }

  @Test fun shouldEmitFormField() {

    val externalFormField: ExternalFormField = mock()

    val onExternalForm: PublishSubject<ExternalFormField> = PublishSubject.create()

    val subscriber = TestSubscriber<ExternalFormField>()
    onExternalForm.subscribe(subscriber)

    val viewModel: FieldExternalViewModel = FieldExternalViewModel(externalFormField, onExternalForm)

    viewModel.onExternalFormAction()

    subscriber.assertValue(externalFormField)

  }

}