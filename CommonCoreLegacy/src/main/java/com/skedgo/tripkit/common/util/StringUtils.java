/*
 * Copyright (c) SkedGo 2013
 */

package com.skedgo.tripkit.common.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {
  private StringUtils() {}

  public static String firstNonEmpty(String... values) {
    if (values == null) {
      return null;
    }

    for (String v : values) {
      if (!TextUtils.isEmpty(v)) {
        return v;
      }
    }

    return null;
  }

  public static String capitalizeFirst(String str) {
    if (TextUtils.isEmpty(str)) {
      return str;
    }

    return Character.toUpperCase(str.charAt(0)) + str.substring(1);
  }

  public static String makeArgsString(int argc) {
    String[] args = new String[argc];
    Arrays.fill(args, "?");
    String result = TextUtils.join(",", args);
    return result;
  }

  /*Join an array of strings by default dilimeter, which is comma*/
  public static String join(ArrayList<String> strings) {
    return join(strings, ",");
  }

  /**
   * Joins the elements of the provided List into a single String containing the provided elements.
   *
   * @param strings   The List of values to join together, may be null
   * @param separator The separator character to use
   * @return The joined String, empty if null List input
   */
  public static String join(List<String> strings, String separator) {
    String s = "";
    if (strings != null) {
      for (String item : strings) {
        s += item + separator;
      }

      int pos = s.lastIndexOf(separator);
      s = s.substring(0, pos).trim();
    }

    return s;
  }

  public static String extractMajorMinorVersion(String version) {
    try {
      Pattern pattern = Pattern.compile("(\\d+\\.\\d+)");
      Matcher matcher = pattern.matcher(version);
      if (matcher.find()) {
        return matcher.group(1);
      } else {
        return version; // Return the original string if no match is found
      }
    } catch (Exception e) {
      return version; // Return the original string if there's an error
    }
  }
}
