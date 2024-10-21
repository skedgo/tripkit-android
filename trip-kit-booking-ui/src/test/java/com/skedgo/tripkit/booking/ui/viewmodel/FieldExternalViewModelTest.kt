// TODO: Unit test - refactor
/* Disabled class due to mockito exception
package com.skedgo.tripkit.booking.ui.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.booking.ExternalFormField
import io.reactivex.subjects.PublishSubject
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FieldExternalViewModelTest {

    @Test
    fun shouldSetTitle() {

        val externalFormField: ExternalFormField = mock()
        whenever(externalFormField.title).thenReturn("title")

        val viewModel: FieldExternalViewModel =
            FieldExternalViewModel(externalFormField, PublishSubject.create())

        assertThat(viewModel.title).isEqualTo("title")

    }

    @Test
    fun shouldSetEmptyTitle() {

        val externalFormField: ExternalFormField = mock()
        whenever(externalFormField.title).thenReturn(null)

        val viewModel: FieldExternalViewModel =
            FieldExternalViewModel(externalFormField, PublishSubject.create())

        assertThat(viewModel.title).isEqualTo("")

    }

    @Test
    fun shouldEmitFormField() {

        val externalFormField: ExternalFormField = mock()

        val onExternalForm: PublishSubject<ExternalFormField> = PublishSubject.create()

        val subscriber = onExternalForm.test()

        val viewModel: FieldExternalViewModel =
            FieldExternalViewModel(externalFormField, onExternalForm)

        viewModel.onExternalFormAction()

        subscriber.assertValue(externalFormField)

    }

}
 */