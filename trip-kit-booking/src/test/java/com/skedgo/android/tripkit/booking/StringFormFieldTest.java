package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class StringFormFieldTest {

  @Test public void Parse() {
    Gson gson = new GsonBuilder().serializeNulls().create();

    // Test 1
    String testParse = "{\n" +
        "                  \"type\": \"string\",\n" +
        "                  \"title\": \"Interior\",\n" +
        "                  \"id\": \"interior\",\n" +
        "                  \"readOnly\": true,\n" +
        "                  \"value\": \"GOOD\",\n" +
        "                  \"keyboardType\": \"TEXT\" \n" +
        "                }";
    StringFormField actual = gson.fromJson(testParse, StringFormField.class);
    assertThat(actual.getTitle()).isEqualTo("Interior");
    assertThat(actual.getId()).isEqualTo("interior");
    assertThat(actual.isReadOnly()).isEqualTo(true);
    assertThat(actual.getType()).isEqualTo("string");
    assertThat(actual.getValue()).isEqualTo("GOOD");
    assertThat(actual.getKeyboardType()).isEqualTo("TEXT");
    assertThat(actual.isHidden()).isEqualTo(false);

    // Test 2
    testParse = "{\n" +
        "                  \"type\": \"string\",\n" +
        "                  \"title\": \"Car\",\n" +
        "                  \"id\": \"car\",\n" +
        "                  \"hidden\": true,\n" +
        "                  \"value\": \"386XFI\",\n" +
        "                  \"keyboardType\": \"TEXT\" \n" +
        "                }";
    actual = gson.fromJson(testParse, StringFormField.class);
    assertThat(actual.getTitle()).isEqualTo("Car");
    assertThat(actual.getId()).isEqualTo("car");
    assertThat(actual.getType()).isEqualTo("string");
    assertThat(actual.isReadOnly()).isEqualTo(false);
    assertThat(actual.getValue()).isEqualTo("386XFI");
    assertThat(actual.getKeyboardType()).isEqualTo("TEXT");
    assertThat(actual.isHidden()).isEqualTo(true);
  }

  @Test public void BooleanParcelable() {
    StringFormField expected = new StringFormField();
    expected.setTitle("a");
    expected.setId("string");
    expected.setHidden(false);
    expected.setReadOnly(false);
    expected.setKeyboardType("TEXT");

    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    StringFormField actual = StringFormField.CREATOR.createFromParcel(parcel);
    assertThat(actual.getId()).isEqualTo(expected.getId());
    assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
    assertThat(actual.isHidden()).isEqualTo(expected.isHidden());
    assertThat(actual.isReadOnly()).isEqualTo(expected.isReadOnly());
    assertThat(actual.getValue()).isEqualTo(expected.getValue());
    assertThat(actual.getKeyboardType()).isEqualTo(expected.getKeyboardType());
  }

  @Test public void StringParcelable() {
    StringFormField expected = new StringFormField();
    expected.setValue("x");
    expected.setId("string");
    expected.setHidden(false);
    expected.setReadOnly(false);
    expected.setKeyboardType("TEXT");

    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    StringFormField actual = StringFormField.CREATOR.createFromParcel(parcel);
    assertThat(actual.getId()).isEqualTo(expected.getId());
    assertThat(actual.getTitle()).isNull();
    assertThat(actual.isHidden()).isEqualTo(expected.isHidden());
    assertThat(actual.isReadOnly()).isEqualTo(expected.isReadOnly());
    assertThat(actual.getValue()).isEqualTo(expected.getValue());
    assertThat(actual.getKeyboardType()).isEqualTo(expected.getKeyboardType());
  }

  @Test public void Null() {
    StringFormField expected = new StringFormField();
    // not set value here, value will be null
    expected.setId("string");
    expected.setHidden(false);
    expected.setReadOnly(false);
    expected.setKeyboardType("TEXT");

    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    StringFormField actual = StringFormField.CREATOR.createFromParcel(parcel);
    assertThat(actual.getId()).isEqualTo(expected.getId());
    assertThat(actual.getTitle()).isNull();
    assertThat(actual.isHidden()).isEqualTo(expected.isHidden());
    assertThat(actual.isReadOnly()).isEqualTo(expected.isReadOnly());
    assertThat(actual.getValue()).isEqualTo(expected.getValue());
    assertThat(actual.getKeyboardType()).isEqualTo(expected.getKeyboardType());
  }
}