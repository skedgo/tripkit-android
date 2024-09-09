package com.skedgo.tripkit.booking;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class InputFormTest {

    @Test
    public void NullInput() {
        InputForm inputForm = InputForm.from(null);
        assertThat(inputForm).isNull();
    }

    @Test
    public void EmptyInput() {
        InputForm inputForm = InputForm.from(new ArrayList<FormGroup>());
        assertThat(inputForm).isNotNull();
        List<FormField> formFieldItemList = inputForm.input();
        assertThat(formFieldItemList).hasSize(0);
    }

    @Test
    public void NonEmptyInput() {

        // Create first form
        List<FormGroup> list = new ArrayList<>();
        FormGroup item1 = new FormGroup();
        item1.setTitle("Requester Passenger");
        List<FormField> list1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            StringFormField item = new StringFormField();
            item.setTitle("First name");
            item.setType("string");
            item.setValue("value");
            item.setId("string");
            list1.add(item);
        }
        item1.setFields(list1);
        list.add(item1);

        // Create second form
        FormGroup item2 = new FormGroup();
        item1.setTitle("Requester Passenger");
        List<FormField> list2 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            StepperFormField item = new StepperFormField();
            item.setTitle("Age");
            item.setType("stepper");
            item.setValue(10);
            item.setId("stepper");
            list2.add(item);
        }
        OptionFormField optionFormField = new OptionFormField();
        optionFormField.setType("option");
        list2.add(optionFormField);
        item2.setFields(list2);
        list.add(item2);

        // Testing
        InputForm inputForm = InputForm.from(list);
        assertThat(inputForm).isNotNull();
        List<FormField> formFieldItemList = inputForm.input();
        assertThat(formFieldItemList).hasSize(6);
        assertThat(formFieldItemList.get(0)).isInstanceOf(StringFormField.class);
        assertThat(formFieldItemList.get(1)).isInstanceOf(StringFormField.class);
        assertThat(formFieldItemList.get(2)).isInstanceOf(StringFormField.class);
        assertThat(formFieldItemList.get(4)).isInstanceOf(StepperFormField.class);
        assertThat(formFieldItemList.get(5)).isInstanceOf(OptionFormField.class);
    }

}