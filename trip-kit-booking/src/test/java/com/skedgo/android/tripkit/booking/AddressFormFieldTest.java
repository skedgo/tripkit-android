package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.*;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
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
}