package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class DateTimeFormFieldTest {

  @Test public void Parcelable() {
    DateTimeFormField expected = new DateTimeFormField();
    expected.setValue(100);
    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    DateTimeFormField actual = DateTimeFormField.CREATOR.createFromParcel(parcel);
    assertThat(actual.getValue()).isEqualTo(expected.getValue());
  }
}