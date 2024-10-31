package com.skedgo.tripkit.booking

import com.skedgo.tripkit.booking.OptionFormField
import com.skedgo.tripkit.booking.StepperFormField
import com.skedgo.tripkit.booking.StringFormField
import org.assertj.core.api.Java6Assertions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class InputFormTest {

    @Test
    fun nullInput() {
        val inputForm = InputForm.from(null)
        assertNull(inputForm)
    }

    @Test
    fun emptyInput() {
        val inputForm = InputForm.from(emptyList<FormGroup>())
        assertNotNull(inputForm)
        val formFieldItemList = inputForm?.input()
        assertEquals(0, formFieldItemList?.size)
    }

    @Test
    fun nonEmptyInput() {
        // Create first form group
        val list = mutableListOf<FormGroup>()
        val item1 = FormGroup().apply {
            title = "Requester Passenger"
            fields = mutableListOf<FormField>().apply {
                for (i in 0 until 3) {
                    add(StringFormField().apply {
                        title = "First name"
                        type = "string"
                        setValue("value")
                        id = "string"
                    })
                }
            }
        }
        list.add(item1)

        // Create second form group
        val item2 = FormGroup().apply {
            title = "Requester Passenger"
            fields = mutableListOf<FormField>().apply {
                for (i in 0 until 2) {
                    add(StepperFormField().apply {
                        title = "Age"
                        type = "stepper"
                        setValue(10)
                        id = "stepper"
                    })
                }
                add(OptionFormField().apply {
                    type = "option"
                })
            }
        }
        list.add(item2)

        // Testing
        val inputForm = InputForm.from(list)
        assertNotNull(inputForm)
        val formFieldItemList = inputForm?.input()
        assertEquals(6, formFieldItemList?.size)
        assert(formFieldItemList?.get(0) is StringFormField)
        assert(formFieldItemList?.get(1) is StringFormField)
        assert(formFieldItemList?.get(2) is StringFormField)
        assert(formFieldItemList?.get(3) is StepperFormField)
        assert(formFieldItemList?.get(4) is StepperFormField)
        assert(formFieldItemList?.get(5) is OptionFormField)
    }
}