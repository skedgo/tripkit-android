package com.skedgo.android.tripkit.booking.viewmodel;

import android.os.Parcel;

import com.skedgo.android.tripkit.booking.BookingAction;
import com.skedgo.android.tripkit.booking.BuildConfig;
import com.skedgo.android.tripkit.booking.InputForm;
import com.skedgo.android.tripkit.booking.LinkFormField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ParamImplTest {

  @Test public void Create() {
    ParamImpl param = ParamImpl.create("url");

    assertThat(param.getMethod())
        .describedAs("Should use method get for just an URL")
        .isEqualTo(LinkFormField.METHOD_GET);
  }

  @Test public void Create1() {
    final LinkFormField linkFormField = new LinkFormField();
    linkFormField.setMethod(LinkFormField.METHOD_POST);
    ParamImpl param = ParamImpl.create(linkFormField);

    assertThat(param.getMethod())
        .describedAs("Should parse method from LinkFormField")
        .isEqualTo(LinkFormField.METHOD_POST);
  }

  @Test public void Create2() {
    final BookingAction bookingAction = new BookingAction();
    final InputForm postBody = new InputForm();
    ParamImpl param = ParamImpl.create(bookingAction, postBody);

    assertThat(param.getMethod())
        .describedAs("Should use method post for BookingAction")
        .isEqualTo(LinkFormField.METHOD_POST);

  }

  @Test public void Parse() {
    final BookingAction bookingAction = new BookingAction();
    final InputForm postBody = new InputForm();
    ParamImpl expected = ParamImpl.create(bookingAction, postBody);

    final Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);

    ParamImpl actual = ParamImpl.CREATOR.createFromParcel(parcel);

    assertThat(actual.getUrl()).isEqualTo(expected.getUrl());
    assertThat(actual.getMethod()).isEqualTo(expected.getMethod());
  }
}