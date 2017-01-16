package com.skedgo.routepersistence;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.TripSegment;

import java.io.IOException;

/**
 * Reads and writes only bare essentials of {@link Location}
 * when persisting {@link TripSegment} as JSON.
 */
public final class LocationTypeAdapterFactory implements TypeAdapterFactory {
  @Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (LocationTypeAdapter.adapts(type)) {
      return (TypeAdapter<T>) new LocationTypeAdapter(gson);
    }
    return null;
  }

  private static class LocationTypeAdapter extends TypeAdapter<Location> {
    LocationTypeAdapter(Gson gson) {}

    static boolean adapts(TypeToken<?> type) {
      return Location.class == type.getRawType();
    }

    @Override public void write(JsonWriter out, Location value) throws IOException {
      if (value == null) {
        out.nullValue();
      } else {
        writeLocation(out, value);
      }
    }

    @Override public Location read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      return readLocation(in);
    }

    private void writeLocation(JsonWriter out, Location instance)
        throws IOException {
      out.beginObject();
      String nameValue = instance.getName();
      if (nameValue != null) {
        out.name("name");
        out.value(nameValue);
      } else if (out.getSerializeNulls()) {
        out.name("name");
        out.nullValue();
      }
      String addressValue = instance.getAddress();
      if (addressValue != null) {
        out.name("address");
        out.value(addressValue);
      } else if (out.getSerializeNulls()) {
        out.name("address");
        out.nullValue();
      }
      String timezoneValue = instance.getTimeZone();
      if (timezoneValue != null) {
        out.name("timezone");
        out.value(timezoneValue);
      } else if (out.getSerializeNulls()) {
        out.name("timezone");
        out.nullValue();
      }
      out.name("lat");
      out.value(instance.getLat());
      out.name("lng");
      out.value(instance.getLon());
      out.name("bearing");
      out.value(instance.getBearing());
      out.endObject();
    }

    private Location readLocation(JsonReader in)
        throws IOException {
      final Location location = new Location();
      in.beginObject();
      while (in.hasNext()) {
        eachAttribute(in, location);
      }
      in.endObject();
      return location;
    }

    private void eachAttribute(JsonReader in, Location location)
        throws IOException {
      String attributeName = in.nextName();
      switch (attributeName.charAt(0)) {
        case 'n':
          if ("name".equals(attributeName)) {
            readInName(in, location);
            return;
          }
          break;
        case 'a':
          if ("address".equals(attributeName)) {
            readInAddress(in, location);
            return;
          }
          break;
        case 't':
          if ("timezone".equals(attributeName)) {
            readInTimezone(in, location);
            return;
          }
          break;
        case 'l':
          if ("lat".equals(attributeName)) {
            readInLat(in, location);
            return;
          }
          if ("lng".equals(attributeName)) {
            readInLng(in, location);
            return;
          }
          break;
        case 'b':
          if ("bearing".equals(attributeName)) {
            readInBearing(in, location);
            return;
          }
          break;
        default:
      }
      in.skipValue();
    }

    private void readInName(JsonReader in, Location instance)
        throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
      } else {
        instance.setName(in.nextString());
      }
    }

    private void readInAddress(JsonReader in, Location instance)
        throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
      } else {
        instance.setAddress(in.nextString());
      }
    }

    private void readInTimezone(JsonReader in, Location instance)
        throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
      } else {
        instance.setTimeZone(in.nextString());
      }
    }

    private void readInLat(JsonReader in, Location instance)
        throws IOException {
      instance.setLat(in.nextDouble());
    }

    private void readInLng(JsonReader in, Location instance)
        throws IOException {
      instance.setLon(in.nextDouble());
    }

    private void readInBearing(JsonReader in, Location instance)
        throws IOException {
      instance.setBearing(in.nextInt());
    }
  }
}
