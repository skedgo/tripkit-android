package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.*;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class LinkFormFieldTest {

  @Test public void Parcelable() {
    LinkFormField expected = new LinkFormField();
    expected.setValue("url");
    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    LinkFormField actual = LinkFormField.CREATOR.createFromParcel(parcel);
    assertThat(actual.getValue()).isEqualTo(expected.getValue());
  }
}