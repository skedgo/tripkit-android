package com.skedgo.android.tripkit.booking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class OptionValueTest {
  @Test public void Parse() {
    Gson gson = new GsonBuilder().serializeNulls().create();
    String testParse = "{\n" +
        "          \"type\": \"OPTION\",\n" +
        "          \"title\": \"Account\",\n" +
        "          \"id\": \"account\",\n" +
        "          \"value\": {\n" +
        "             \"title\": \"100 - Test Account 1\",\n" +
        "             \"value\": \"100 - Test Account 1\" \n" +
        "           },\n" +
        "          \"allValues\": [\n" +
        "            {\n" +
        "             \"title\": \"100 - Test Account 1\",\n" +
        "             \"value\": \"100 - Test Account 1\" \n" +
        "             },\n" +
        "            {\n" +
        "             \"title\": \"200 - Test Account 1\",\n" +
        "             \"value\": \"200 - Test Account 2\" \n" +
        "           }\n" +
        "          ]\n" +
        "        }";
    OptionFormField actual = gson.fromJson(testParse, OptionFormField.class);
    assertThat(actual.getType()).isEqualTo("OPTION");
    assertThat(actual.getTitle()).isEqualTo("Account");
    assertThat(actual.getId()).isEqualTo("account");
    assertThat(actual.getValue().getTitle()).isEqualTo("100 - Test Account 1");
    assertThat(actual.getValue().getValue()).isEqualTo("100 - Test Account 1");

    List<OptionFormField.OptionValue> allValues = actual.getAllValues();
    assertThat(allValues).hasSize(2);
    assertThat(allValues.get(0).getTitle()).isEqualTo("100 - Test Account 1");
    assertThat(allValues.get(0).getValue()).isEqualTo("100 - Test Account 1");
    assertThat(allValues.get(1).getTitle()).isEqualTo("200 - Test Account 1");
    assertThat(allValues.get(1).getValue()).isEqualTo("200 - Test Account 2");
  }
}