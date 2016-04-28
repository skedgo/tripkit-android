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
public class AddressFormFieldTest {
  @Test public void Parse() {
    String test = "{\n" +
        "                  \"type\": \"address\",\n" +
        "                  \"title\": \"Location\",\n" +
        "                  \"id\": \"location\",\n" +
        "                  \"readOnly\": true,\n" +
        "                  \"value\": {\n" +
        "                    \"lat\": 39.76256,\n" +
        "                    \"lng\": -105.03584,\n" +
        "                    \"address\": \"Meade St 3225, 80211 Denver\",\n" +
        "                    \"name\": \"Meade St 3225, 80211 Denver\" \n" +
        "                  }\n" +
        "                }";
    Gson gson = new GsonBuilder().serializeNulls().create();
    AddressFormField actual = gson.fromJson(test, AddressFormField.class);

    assertThat(actual.getType()).isEqualTo("address");
    assertThat(actual.getId()).isEqualTo("location");
    assertThat(actual.getTitle()).isEqualTo("Location");
    assertThat(actual.isReadOnly()).isEqualTo(true);

    AddressFormField.Address address = actual.getValue();
    assertThat(address.getAddress()).isEqualTo("Meade St 3225, 80211 Denver");
    assertThat(address.getName()).isEqualTo("Meade St 3225, 80211 Denver");
    assertThat(address.getLatitude()).isEqualTo(39.76256);
    assertThat(address.getLongitude()).isEqualTo(-105.03584);
  }

  @Test public void Parcelable() {
    AddressFormField expected = new AddressFormField();
    AddressFormField.Address address = new AddressFormField.Address();
    address.setLatitude(10.0);
    address.setLongitude(12.0);
    expected.setValue(address);
    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    AddressFormField actual = AddressFormField.CREATOR.createFromParcel(parcel);
    AddressFormField.Address actualAdd = actual.getValue();
    assertThat(actualAdd.getLatitude()).isEqualTo(10.0);
    assertThat(actualAdd.getLongitude()).isEqualTo(12.0);
  }
}