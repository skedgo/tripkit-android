package com.skedgo.android.common.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.skedgo.android.common.model.Region;

import java.lang.reflect.Type;

final class CityJsonDeserializer implements JsonDeserializer<Region.City> {
  @Override
  public Region.City deserialize(JsonElement json,
                                 Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {
    final JsonObject jsonCity = json.getAsJsonObject();
    final String title = jsonCity.getAsJsonPrimitive("title").getAsString();
    final double lng = jsonCity.getAsJsonPrimitive("lng").getAsDouble();
    final double lat = jsonCity.getAsJsonPrimitive("lat").getAsDouble();

    final Region.City city = new Region.City();
    city.setName(title);
    city.setLat(lat);
    city.setLon(lng);
    return city;
  }
}