package com.skedgo.android.tripkit.booking.model;

import android.os.Parcel;

import com.skedgo.android.tripkit.booking.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
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