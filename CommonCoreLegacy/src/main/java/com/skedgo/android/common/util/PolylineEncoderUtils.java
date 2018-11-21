package com.skedgo.android.common.util;

import android.text.TextUtils;

import com.skedgo.android.common.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Use `PolyUtil` from https://github.com/googlemaps/android-maps-utils instead.
 */
@Deprecated
public class PolylineEncoderUtils {
  private static final String TAG = LogUtils.makeTag(PolylineEncoderUtils.class);

  private static StringBuilderPool sSbPool = new StringBuilderPool();

  /**
   * Kudos: http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
   *
   * @param encoded A string encoding of a polyline as output by the Encoded Polyline Algorithm Format
   * @return a list of decoded coordinates
   */
  public static List<TripKitLatLng> decode(String encoded) {
    if (TextUtils.isEmpty(encoded)) {
      return null;
    }

    try {
      List<TripKitLatLng> coords = new ArrayList<TripKitLatLng>();
      int len = encoded.length();
      int index = 0;
      int lat = 0;
      int lng = 0;

      while (index < len) {
        int b, shift = 0, result = 0;
        do {
          b = encoded.charAt(index++) - 63;
          result |= (b & 0x1f) << shift;
          shift += 5;
        } while (b >= 0x20);
        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
        lat += dlat;

        shift = 0;
        result = 0;
        do {
          b = encoded.charAt(index++) - 63;
          result |= (b & 0x1f) << shift;
          shift += 5;
        } while (b >= 0x20);
        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
        lng += dlng;

        coords.add(new TripKitLatLng(lat / 1E5, lng / 1E5));
      }

      return coords;
    } catch (Exception e) {
      LogUtils.LOGD(TAG, "Error decoding polyline string: " + encoded);
    }

    return null;
  }

  public static String encode(List<Double> coords) {
    if (coords == null || coords.isEmpty()) {
      return null;
    }

    StringBuilder result = sSbPool.retrieve();

    int plat = 0;
    int plng = 0;
    for (int i = 0, size = coords.size(); i < size; i += 2) {
      int late5 = floor1e5(coords.get(i));
      int lnge5 = floor1e5(coords.get(i + 1));

      int dlat = late5 - plat;
      int dlng = lnge5 - plng;

      plat = late5;
      plng = lnge5;

      result.append(encodeSignedNumber(dlat)).append(encodeSignedNumber(dlng));
    }

    try {
      return result.toString();
    } finally {
      result.setLength(0);
      sSbPool.save(result);
    }
  }

  private static int floor1e5(double coordinate) {
    return (int) Math.floor(coordinate * 1e5);
  }

  private static String encodeSignedNumber(int num) {
    int sgn_num = num << 1;
    if (num < 0) {
      sgn_num = ~(sgn_num);
    }
    return (encodeNumber(sgn_num));
  }

  private static String encodeNumber(int num) {

    StringBuilder encodeString = sSbPool.retrieve();

    while (num >= 0x20) {
      int nextValue = (0x20 | (num & 0x1f)) + 63;
      encodeString.append((char) (nextValue));
      num >>= 5;
    }

    num += 63;
    encodeString.append((char) (num));

    try {
      return encodeString.toString();
    } finally {
      encodeString.setLength(0);
      sSbPool.save(encodeString);
    }
  }
}
