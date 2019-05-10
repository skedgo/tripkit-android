package com.skedgo.android.tripkit.booking.viewmodel;

import android.os.Parcel;

import com.skedgo.android.tripkit.booking.BookingAction;
import com.skedgo.android.tripkit.booking.InputForm;
import com.skedgo.android.tripkit.booking.LinkFormField;
import com.skedgo.android.tripkit.booking.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class ParamTest {
  @Test public void Create() {
    Param param = Param.create("url");

    assertThat(param.getMethod())
        .describedAs("Should use method get for just an URL")
        .isEqualTo(LinkFormField.METHOD_GET);
  }

  @Test public void Create1() {
    final LinkFormField linkFormField = new LinkFormField();
    linkFormField.setMethod(LinkFormField.METHOD_POST);
    Param param = Param.create(linkFormField);

    assertThat(param.getMethod())
        .describedAs("Should parse method from LinkFormField")
        .isEqualTo(LinkFormField.METHOD_POST);
  }

  @Test public void Create2() {
    final BookingAction bookingAction = new BookingAction();
    final InputForm postBody = new InputForm();
    Param param = Param.create(bookingAction, postBody);

    assertThat(param.getMethod())
        .describedAs("Should use method post for BookingAction")
        .isEqualTo(LinkFormField.METHOD_POST);
  }

  @Test public void Parse() {
    final BookingAction bookingAction = new BookingAction();
    final InputForm postBody = new InputForm();
    Param expected = Param.create(bookingAction, postBody);

    final Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);

    Param actual = Param.CREATOR.createFromParcel(parcel);

    assertThat(actual.getUrl()).isEqualTo(expected.getUrl());
    assertThat(actual.getMethod()).isEqualTo(expected.getMethod());
  }
}