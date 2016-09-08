package com.skedgo.android.tripkit.booking;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Credit to http://stackoverflow.com/questions/5800433/polymorphism-with-gson.
 */
public final class FormFieldJsonAdapter implements JsonDeserializer<FormField>, JsonSerializer<FormField> {
  private static final String TYPE = "type";

  @Override
  public FormField deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    JsonPrimitive prim = (JsonPrimitive) jsonObject.get(TYPE);
    String type = prim.getAsString().toUpperCase();
    String className = fromTypeToClassName(type);

    Class<?> klass;
    try {
      klass = Class.forName(className);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new JsonParseException(e.getMessage());
    }
    return context.deserialize(jsonObject, klass);
  }

  @Override
  public JsonElement serialize(FormField src, Type typeOfSrc, JsonSerializationContext context) {
    String type = src.getType().toUpperCase();
    String className = fromTypeToClassName(type);

    Class<?> klass;
    try {
      klass = Class.forName(className);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new JsonParseException(e.getMessage());
    }
    return context.serialize(src, klass);
  }

  private String fromTypeToClassName(String type) {
    String className = LinkFormField.class.getName();
    switch (type) {
      case "STRING":
      case "NUMBER":
      case "TEXT":
        className = StringFormField.class.getName();
        break;
      case "PASSWORD":
        className = PasswordFormField.class.getName();
        break;
      case "OPTION":
        className = OptionFormField.class.getName();
        break;
      case "LINK":
        className = LinkFormField.class.getName();
        break;
      case "ADDRESS":
        className = AddressFormField.class.getName();
        break;
      case "STEPPER":
        className = StepperFormField.class.getName();
        break;
      case "DATETIME":
        className = DateTimeFormField.class.getName();
        break;
      case "BOOKINGFORM":
        className = BookingForm.class.getName();
        break;
      case "SWITCH":
        className = SwitchFormField.class.getName();
        break;
    }
    return className;
  }
}