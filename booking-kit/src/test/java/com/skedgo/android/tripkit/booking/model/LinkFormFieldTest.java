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