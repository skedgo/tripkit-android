package com.skedgo.android.bookingkit.model;

import android.os.Parcel;

import com.skedgo.android.bookingkit.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SwitchFormFieldTest {

  @Test public void Parcelable() {
    SwitchFormField expected = new SwitchFormField();
    expected.setValue(true);
    expected.setKeyboardType("TEXT");
    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    SwitchFormField actual = SwitchFormField.CREATOR.createFromParcel(parcel);
    assertThat(actual.getValue()).isEqualTo(expected.getValue());
    assertThat(actual.getKeyboardType()).isEqualTo(expected.getKeyboardType());
  }
}
