package com.skedgo.android.bookingkit.model;

import android.os.Parcel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.skedgo.android.bookingkit.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.StringReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class BookingFormTest {

  private Gson gson;

  @Test public void Parse() {

    // Test 1
    String testParse = "{\n" +
        "  \"form\": [\n" +
        "    {\n" +
        "      \"fields\": [\n" +
        "        {\n" +
        "          \"type\": \"DATETIME\",\n" +
        "          \"title\": \"Time\",\n" +
        "          \"id\": \"time\",\n" +
        "          \"value\": 1410871325000\n" +
        "        }\n" +
        "      ]\n" +
        "    }\n" +
        "  ],\n" +
        "  \"action\": {\n" +
        "    \"title\": \"Done\"\n" +
        "  },\n" +
        "  \"title\": \"Car2Go\",\n" +
        "  \"subtitle\": \"Tue 16 22:42\",\n" +
        "  \"message\": \"A confirmation email has been sent\" \n" +
        "}";
    JsonReader reader = new JsonReader(new StringReader(testParse));
    reader.setLenient(true);

    BookingForm actual = gson.fromJson(reader, BookingForm.class);
    assertThat(actual.getTitle()).isEqualTo("Car2Go");
    assertThat(actual.getSubtitle()).isEqualTo("Tue 16 22:42");
    assertThat(actual.getForm()).hasSize(1);

    FormGroup item = actual.getForm().get(0);
    assertThat(item.getFields()).hasSize(1);
    assertThat(item.getFields().get(0)).isInstanceOf(DateTimeFormField.class);

    // Test 2
    testParse = "{\"type\":\"bookingForm\",\"title\":\"Login to Car2Go\",\"action\":{\"title\":\"Next\",\"enabled\":false,\"url\":\"http://BARYOGENESIS.SKEDGO.COM/satapp/booking/cba0b388-4b8e-4812-b287-c7302327c794/book\"},\"form\":[{\"title\":\"Authorize\",\"fields\":[{\"type\":\"link\",\"title\":\"Go to Car2Go\",\"id\":\"oauth\",\"type\":\"link\",\"value\":\"https://www.car2go.com/api/authorize?oauth_token=QAd66gisPl80PIDy1sywFUHB\"}]}]}\n";
    reader = new JsonReader(new StringReader(testParse));
    reader.setLenient(true);
    actual = gson.fromJson(reader, BookingForm.class);
    assertThat(actual.getForm()).isNotNull();

    // Test 3
    testParse = "{\n" +
        "  \"type\": \"bookingForm\",\n" +
        "  \"title\": \"Car2Go\",\n" +
        "  \"form\": [\n" +
        "    {\n" +
        "      \"title\": \"Vehicle\",\n" +
        "      \"fields\": [\n" +
        "        {\n" +
        "          \"type\": \"bookingForm\",\n" +
        "          \"title\": \"SmartCar 690XBP\",\n" +
        "          \"action\": {\n" +
        "            \"title\": \"Next\",\n" +
        "            \"url\": \"http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book\" \n" +
        "          },\n" +
        "          \"form\": [\n" +
        "            {\n" +
        "              \"title\": \"Trip\",\n" +
        "              \"fields\": [\n" +
        "                {\n" +
        "                  \"type\": \"string\",\n" +
        "                  \"title\": \"Car\",\n" +
        "                  \"id\": \"car\",\n" +
        "                  \"hidden\": true,\n" +
        "                  \"value\": \"690XBP\",\n" +
        "                  \"keyboardType\": \"TEXT\" \n" +
        "                },\n" +
        "                {\n" +
        "                  \"type\": \"address\",\n" +
        "                  \"title\": \"Location\",\n" +
        "                  \"id\": \"location\",\n" +
        "                  \"readOnly\": true,\n" +
        "                  \"value\": {\n" +
        "                    \"lat\": 39.75741,\n" +
        "                    \"lng\": -105.03937,\n" +
        "                    \"address\": \"Perry St 2805, 80212 Denver\",\n" +
        "                    \"name\": \"Perry St 2805, 80212 Denver\" \n" +
        "                  }\n" +
        "                },\n" +
        "                {\n" +
        "                  \"type\": \"string\",\n" +
        "                  \"title\": \"Interior\",\n" +
        "                  \"id\": \"interior\",\n" +
        "                  \"readOnly\": true,\n" +
        "                  \"value\": \"GOOD\",\n" +
        "                  \"keyboardType\": \"TEXT\" \n" +
        "                },\n" +
        "                {\n" +
        "                  \"type\": \"string\",\n" +
        "                  \"title\": \"Exterior\",\n" +
        "                  \"id\": \"exterior\",\n" +
        "                  \"readOnly\": true,\n" +
        "                  \"value\": \"GOOD\",\n" +
        "                  \"keyboardType\": \"TEXT\" \n" +
        "                },\n" +
        "                {\n" +
        "                  \"type\": \"string\",\n" +
        "                  \"title\": \"Engine\",\n" +
        "                  \"id\": \"engine\",\n" +
        "                  \"readOnly\": true,\n" +
        "                  \"value\": \"CE\",\n" +
        "                  \"keyboardType\": \"TEXT\" \n" +
        "                },\n" +
        "                {\n" +
        "                  \"type\": \"string\",\n" +
        "                  \"title\": \"Fuel\",\n" +
        "                  \"id\": \"fuel\",\n" +
        "                  \"readOnly\": true,\n" +
        "                  \"value\": \"100\",\n" +
        "                  \"keyboardType\": \"TEXT\" \n" +
        "                }\n" +
        "              ]\n" +
        "            }\n" +
        "          ],\n" +
        "          \"subtitle\": \"Selected Car\" \n" +
        "        },\n" +
        "        {\n" +
        "          \"type\": \"bookingForm\",\n" +
        "          \"title\": \"SmartCar 386XFI\",\n" +
        "          \"action\": {\n" +
        "            \"title\": \"Next\",\n" +
        "            \"url\": \"http://bb-server.buzzhives.com/satapp-debug/booking/d2a68ee5-7fec-4f6b-be89-e04c2676b0d8/book\" \n" +
        "          },\n" +
        "          \"form\": [\n" +
        "            {\n" +
        "              \"title\": \"Trip\",\n" +
        "              \"fields\": [\n" +
        "                {\n" +
        "                  \"type\": \"string\",\n" +
        "                  \"title\": \"Car\",\n" +
        "                  \"id\": \"car\",\n" +
        "                  \"hidden\": true,\n" +
        "                  \"value\": \"386XFI\",\n" +
        "                  \"keyboardType\": \"TEXT\" \n" +
        "                },\n" +
        "                {\n" +
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
        "                },\n" +
        "                {\n" +
        "                  \"type\": \"string\",\n" +
        "                  \"title\": \"Interior\",\n" +
        "                  \"id\": \"interior\",\n" +
        "                  \"readOnly\": true,\n" +
        "                  \"value\": \"GOOD\",\n" +
        "                  \"keyboardType\": \"TEXT\" \n" +
        "                },\n" +
        "                {\n" +
        "                  \"type\": \"string\",\n" +
        "                  \"title\": \"Exterior\",\n" +
        "                  \"id\": \"exterior\",\n" +
        "                  \"readOnly\": true,\n" +
        "                  \"value\": \"GOOD\",\n" +
        "                  \"keyboardType\": \"TEXT\" \n" +
        "                },\n" +
        "                {\n" +
        "                  \"type\": \"string\",\n" +
        "                  \"title\": \"Engine\",\n" +
        "                  \"id\": \"engine\",\n" +
        "                  \"readOnly\": true,\n" +
        "                  \"value\": \"CE\",\n" +
        "                  \"keyboardType\": \"TEXT\" \n" +
        "                },\n" +
        "                {\n" +
        "                  \"type\": \"string\",\n" +
        "                  \"title\": \"Fuel\",\n" +
        "                  \"id\": \"fuel\",\n" +
        "                  \"readOnly\": true,\n" +
        "                  \"value\": \"51\",\n" +
        "                  \"keyboardType\": \"TEXT\" \n" +
        "                }\n" +
        "              ]\n" +
        "            }\n" +
        "          ],\n" +
        "          \"subtitle\": \"1/4 mile away from selected car\" \n" +
        "        }\n" +
        "      ]\n" +
        "    }\n" +
        "  ],\n" +
        "  \"subtitle\": \"From $10.57\" \n" +
        "}";
    reader = new JsonReader(new StringReader(testParse));
    reader.setLenient(true);

    actual = gson.fromJson(reader, BookingForm.class);
    assertThat(actual.getRefreshURLForSourceObject()).isNull();
    assertThat(actual.getTitle()).isEqualTo("Car2Go");
    assertThat(actual.getSubtitle()).isEqualTo("From $10.57");
    assertThat(actual.getForm()).hasSize(1);

    List<FormField> items = actual.getForm().get(0).getFields();
    assertThat(items.get(0)).isInstanceOf(BookingForm.class);
    assertThat(items.get(1)).isInstanceOf(BookingForm.class);
  }

  @Test public void Parcelable() {
    String testParse = "{\n" +
        "  \"type\": \"bookingForm\",\n" +
        "  \"title\": \"Jay Ride Shuttle\",\n" +
        "  \"action\": {\n" +
        "    \"title\": \"Next\",\n" +
        "    \"url\": \"http://bb-server.buzzhives.com/satapp-debug/booking/0000000a-000a-000a-000a-00000000000a/book\"\n" +
        "  },\n" +
        "  \"form\": [\n" +
        "    {\n" +
        "      \"title\": \"Trip\",\n" +
        "      \"fields\": [\n" +
        "        {\n" +
        "          \"type\": \"address\",\n" +
        "          \"title\": \"From\",\n" +
        "          \"id\": \"from\",\n" +
        "          \"value\": {\n" +
        "            \"lat\": -33.8004829805011,\n" +
        "            \"lng\": 151.283876064691,\n" +
        "            \"address\": \"Manly Wharf\",\n" +
        "            \"name\": \"Manly Wharf\"\n" +
        "          }\n" +
        "        },\n" +
        "        {\n" +
        "          \"type\": \"address\",\n" +
        "          \"title\": \"To\",\n" +
        "          \"id\": \"to\",\n" +
        "          \"value\": {\n" +
        "            \"lat\": -33.936635,\n" +
        "            \"lng\": 151.170696,\n" +
        "            \"address\": \"International Airport Station Platform 1\",\n" +
        "            \"name\": \"International Airport Station Platform 1\"\n" +
        "          }\n" +
        "        },\n" +
        "        {\n" +
        "          \"type\": \"datetime\",\n" +
        "          \"title\": \"When\",\n" +
        "          \"id\": \"depart\",\n" +
        "          \"value\": 1431558542\n" +
        "        }\n" +
        "      ]\n" +
        "    },\n" +
        "    {\n" +
        "      \"title\": \"Ticket\",\n" +
        "      \"fields\": [\n" +
        "        {\n" +
        "          \"type\": \"stepper\",\n" +
        "          \"title\": \"Adult\",\n" +
        "          \"id\": \"adult\",\n" +
        "          \"required\": true,\n" +
        "          \"value\": 1,\n" +
        "          \"minValue\": 0,\n" +
        "          \"maxValue\": 15\n" +
        "        },\n" +
        "        {\n" +
        "          \"type\": \"stepper\",\n" +
        "          \"title\": \"Child\",\n" +
        "          \"id\": \"child\",\n" +
        "          \"required\": false,\n" +
        "          \"value\": 0,\n" +
        "          \"minValue\": 0,\n" +
        "          \"maxValue\": 15\n" +
        "        },\n" +
        "        {\n" +
        "          \"type\": \"option\",\n" +
        "          \"title\": \"Type\",\n" +
        "          \"id\": \"ticket_type\",\n" +
        "          \"value\": {\n" +
        "            \"title\": \"One way\",\n" +
        "            \"value\": \"one_way\"\n" +
        "          },\n" +
        "          \"allValues\": [\n" +
        "            {\n" +
        "              \"title\": \"One way\",\n" +
        "              \"value\": \"one_way\"\n" +
        "            },\n" +
        "            {\n" +
        "              \"title\": \"Return\",\n" +
        "              \"value\": \"return\"\n" +
        "            }\n" +
        "          ]\n" +
        "        }\n" +
        "      ]\n" +
        "    }\n" +
        "  ],\n" +
        "  \"subtitle\": \"Tickets AUD14.00 for this trip\"\n" +
        "}";
    JsonReader reader = new JsonReader(new StringReader(testParse));
    reader.setLenient(true);
    BookingForm expected = gson.fromJson(reader, BookingForm.class);

    Parcel parcel = Parcel.obtain();
    expected.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    BookingForm actual = BookingForm.CREATOR.createFromParcel(parcel);

    BookingAction actualAction = actual.getAction();
    assertThat(actualAction.getUrl()).isEqualTo(expected.getAction().getUrl());
    assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
    assertThat(actual.getRefreshURLForSourceObject()).isEqualTo(expected.getRefreshURLForSourceObject());
    List<FormGroup> list = actual.getForm();
    assertThat(list).hasSize(2);

    FormGroup formGroup1 = list.get(0);
    assertThat(formGroup1.getFields()).hasSize(3);
    assertThat(formGroup1.getFields().get(0)).isInstanceOf(AddressFormField.class);
    assertThat(formGroup1.getFields().get(0).getTitle()).isEqualTo("From");
    assertThat(formGroup1.getFields().get(1)).isInstanceOf(AddressFormField.class);
    assertThat(formGroup1.getFields().get(1).getTitle()).isEqualTo("To");
    assertThat(formGroup1.getFields().get(2)).isInstanceOf(DateTimeFormField.class);
    assertThat(formGroup1.getFields().get(2).getTitle()).isEqualTo("When");

    FormGroup formGroup2 = list.get(1);
    assertThat(formGroup2.getFields()).hasSize(3);
    assertThat(formGroup2.getFields().get(0)).isInstanceOf(StepperFormField.class);
    assertThat(formGroup2.getFields().get(0).getTitle()).isEqualTo("Adult");
    assertThat(formGroup2.getFields().get(1)).isInstanceOf(StepperFormField.class);
    assertThat(formGroup2.getFields().get(1).getTitle()).isEqualTo("Child");
    assertThat(formGroup2.getFields().get(2)).isInstanceOf(OptionFormField.class);
    assertThat(formGroup2.getFields().get(2).getTitle()).isEqualTo("Type");
  }

  @Before public void before() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(FormField.class, new FormFieldJsonAdapter());
    gson = builder.create();
  }
}