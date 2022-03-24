package com.skedgo.tripkit.booking;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
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
