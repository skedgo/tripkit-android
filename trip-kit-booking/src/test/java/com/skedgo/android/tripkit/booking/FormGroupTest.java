package com.skedgo.android.tripkit.booking;

import android.os.Parcel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class FormGroupTest {
  @Test public void Parse() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(FormField.class, new FormFieldJsonAdapter());
    Gson gson = builder.create();

    String testParse = " {\n" +
        "      \"title\": \"Insurance\",\n" +
        "      \"footer\": \"I wish to purchase excess reimbursement insurance of 13.95per day = 13.95 (Approx 13.95) Please Note: Excess insurance is billed separately on your credit card statement.\",\n" +
        "      \"fields\": [\n" +
        "        {\n" +
        "          \"type\": \"switch\",\n" +
        "          \"id\": \"insuranceOption\",\n" +
        "          \"title\": \"Annual Excess Insurance\",\n" +
        "          \"value\": false\n" +
        "        }\n" +
        "      ]\n" +
        "    }\n";
    JsonReader reader = new JsonReader(new StringReader(testParse));
    reader.setLenient(true);

    FormGroup actual = gson.fromJson(reader, FormGroup.class);
    assertThat(actual.getTitle()).isEqualTo("Insurance");
    assertThat(actual.getFooter()).isEqualTo("I wish to purchase excess reimbursement insurance of 13.95per day = 13.95 (Approx 13.95) Please Note: Excess insurance is billed separately on your credit card statement.");
    assertThat(actual.getFields()).hasSize(1);
    assertThat(actual.getFields().get(0)).isInstanceOf(SwitchFormField.class);

    testParse = "{\n" +
        "  \"title\": \"Ticket\",\n" +
        "  \"fields\": [\n" +
        "    {\n" +
        "      \"type\": \"stepper\",\n" +
        "      \"title\": \"Adult\",\n" +
        "      \"id\": \"adult\",\n" +
        "      \"required\": true,\n" +
        "      \"value\": 1,\n" +
        "      \"minValue\": 0,\n" +
        "      \"maxValue\": 15\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"stepper\",\n" +
        "      \"title\": \"Child\",\n" +
        "      \"id\": \"child\",\n" +
        "      \"required\": false,\n" +
        "      \"value\": 0,\n" +
        "      \"minValue\": 0,\n" +
        "      \"maxValue\": 15\n" +
        "    },\n" +
        "    {\n" +
        "      \"type\": \"option\",\n" +
        "      \"title\": \"Type\",\n" +
        "      \"id\": \"ticket_type\",\n" +
        "      \"value\": {\n" +
        "        \"title\": \"One way\",\n" +
        "        \"value\": \"one_way\"\n" +
        "      },\n" +
        "      \"allValues\": [\n" +
        "        {\n" +
        "          \"title\": \"One way\",\n" +
        "          \"value\": \"one_way\"\n" +
        "        },\n" +
        "        {\n" +
        "          \"title\": \"Return\",\n" +
        "          \"value\": \"return\"\n" +
        "        }\n" +
        "      ]\n" +
        "    }\n" +
        "  ]\n" +
        "}";
    reader = new JsonReader(new StringReader(testParse));
    reader.setLenient(true);

    actual = gson.fromJson(reader, FormGroup.class);
    assertThat(actual.getTitle()).isEqualTo("Ticket");
    assertThat(actual.getFields()).hasSize(3);
    assertThat(actual.getFields().get(0)).isInstanceOf(StepperFormField.class);
    assertThat(actual.getFields().get(1)).isInstanceOf(StepperFormField.class);
    assertThat(actual.getFields().get(2)).isInstanceOf(OptionFormField.class);
  }

  @Test public void Parcelable() {
    FormGroup expect = new FormGroup();
    List<FormField> list = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      StringFormField item = new StringFormField();
      item.setValue("" + i);
      list.add(item);
    }
    LinkFormField item = new LinkFormField();
    item.setValue("a");
    list.add(item);
    SwitchFormField switchFormField = new SwitchFormField();
    switchFormField.setValue(false);
    list.add(switchFormField);

    expect.setFields(list);
    Parcel parcel = Parcel.obtain();
    expect.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    FormGroup actual = FormGroup.CREATOR.createFromParcel(parcel);
    assertThat(actual.getFields()).hasSize(5);
    assertThat(actual.getFields().get(0)).isInstanceOf(StringFormField.class);
    assertThat(actual.getFields().get(0).getValue()).isEqualTo("0");
    assertThat(actual.getFields().get(1)).isInstanceOf(StringFormField.class);
    assertThat(actual.getFields().get(1).getValue()).isEqualTo("1");
    assertThat(actual.getFields().get(2)).isInstanceOf(StringFormField.class);
    assertThat(actual.getFields().get(2).getValue()).isEqualTo("2");
    assertThat(actual.getFields().get(3)).isInstanceOf(LinkFormField.class);
    assertThat(actual.getFields().get(3).getValue()).isEqualTo("a");
    assertThat(actual.getFields().get(4)).isInstanceOf(SwitchFormField.class);
    assertThat(actual.getFields().get(4).getValue()).isEqualTo(false);
  }
}