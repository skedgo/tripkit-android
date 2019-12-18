package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class BookingActionTest {
  @Test public void Parse() {
    Gson gson = new GsonBuilder().serializeNulls().create();

    String testJson = "{\n" +
        "            \"title\": \"Next\",\n" +
        "            \"url\": \"http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book\" \n" +
        "          }";
    BookingAction actual = gson.fromJson(testJson, BookingAction.class);
    assertThat(actual.getTitle()).isEqualTo("Next");
    assertThat(actual.isEnable()).isEqualTo(true);
    assertThat(actual.isDone()).isEqualTo(false);
    assertThat(actual.getUrl()).isEqualTo("http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book");

    testJson = "{\n" +
        "    \"title\": \"Next\",\n" +
        "    \"enabled\": false,\n" +
        "    \"url\": \"http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book\" \n" +
        "  }";
    actual = gson.fromJson(testJson, BookingAction.class);
    assertThat(actual.getTitle()).isEqualTo("Next");
    assertThat(actual.isEnable()).isEqualTo(false);
    assertThat(actual.isDone()).isEqualTo(false);
    assertThat(actual.getUrl()).isEqualTo("http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book");

    testJson = "{\n" +
        "            \"title\": \"Next\",\n" +
        "            \"done\": true,\n" +
        "            \"url\": \"http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book\" \n" +
        "          }";
    actual = gson.fromJson(testJson, BookingAction.class);
    assertThat(actual.getTitle()).isEqualTo("Next");
    assertThat(actual.isEnable()).isEqualTo(true);
    assertThat(actual.isDone()).isEqualTo(true);
    assertThat(actual.getUrl()).isEqualTo("http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book");

  }

  @Test public void Parcelable() {
    BookingAction expected = new BookingAction();
    expected.setUrl("url");
    expected.setEnable(false);
    expected.setTitle("title");
    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    BookingAction actual = BookingAction.CREATOR.createFromParcel(parcel);
    assertThat(actual.getUrl()).isEqualTo(expected.getUrl());
    assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
    assertThat(actual.isEnable()).isEqualTo(expected.isEnable());
  }
}