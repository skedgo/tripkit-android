package com.skedgo.tripkit.booking;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class StepperFormFieldTest {

  @Test public void Parcelable() {
    StepperFormField expected = new StepperFormField();
    expected.setValue(5);
    expected.setMinValue(0);
    expected.setMaxValue(10);
    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    StepperFormField actual = StepperFormField.CREATOR.createFromParcel(parcel);
    assertThat(actual.getValue()).isEqualTo(expected.getValue());
    assertThat(actual.getMinValue()).isEqualTo(expected.getMinValue());
    assertThat(actual.getMaxValue()).isEqualTo(expected.getMaxValue());
  }
}