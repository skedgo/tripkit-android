package com.skedgo.tripkit.booking.viewmodel

import com.skedgo.tripkit.booking.OptionFormField
import com.skedgo.tripkit.booking.OptionFormField.OptionValue
import junit.framework.TestCase.assertEquals
import org.assertj.core.api.Java6Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class OptionFieldViewModelTest {

    @Test
    fun shouldShowInitValueProperly() {
        val viewModel = OptionFieldViewModel.create(generateOptionFormField())

        assertEquals("Should show the Title in optionValue", "Title", viewModel.getPrimaryText())
        assertEquals("Should show the Value in optionValue", "Value", viewModel.getSecondaryText())
    }

    @Test
    fun shouldShowSelectedValueProperly() {
        val viewModel = OptionFieldViewModel.create(generateOptionFormField())

        viewModel.select(1)
        assertEquals("Should show the Title of optionValue at index 1", "Title 1", viewModel.getPrimaryText())
        assertEquals("Should show the Value of optionValue at index 1", "Value 1", viewModel.getSecondaryText())

        viewModel.select(2)
        assertEquals("Should show the Title of optionValue at index 2", "Title 2", viewModel.getPrimaryText())
        assertEquals("Should show the Value of optionValue at index 2", "Value 2", viewModel.getSecondaryText())
    }

    private fun generateOptionFormField(): OptionFormField {
        val optionValue = OptionFormField.OptionValue("Title", "Value")
        val allValues = mutableListOf(
            optionValue,
            OptionFormField.OptionValue("Title 1", "Value 1"),
            OptionFormField.OptionValue("Title 2", "Value 2"),
            OptionFormField.OptionValue("Title 3", "Value 3")
        )

        return OptionFormField().apply {
            value = optionValue
            setAllValues(allValues)
        }
    }
}
