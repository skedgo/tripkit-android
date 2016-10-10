/*
 * Copyright (c) SkedGo 2013
 */

package com.skedgo.android.common.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

  public static String getNameAndAddressOfLocation(String name, String address) {
    StringBuilder builder = new StringBuilder();
    String nameAndAddress;
    if (!TextUtils.isEmpty(name)) {
      builder.append(name);
      builder.append(",");
    }
    if (!TextUtils.isEmpty(address)) {
      nameAndAddress = builder.append(address).toString();
    } else {
      nameAndAddress = "";
    }
    return nameAndAddress;
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

  /*Join an array of strings by  dilimeter*/
  public static String join(String[] strings, String dilimeter) {
    String s = "";
    for (String s1 : strings) {
      s += s1 + dilimeter;
    }
    int pos = s.lastIndexOf(dilimeter);
    s = s.substring(0, pos).trim();
    return s;
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

  /*Join an array of strings by default dilimeter, which is comma*/
  public static String join(int[] args) {
    if (args == null || args.length == 0) {
      return "";
    }
    return join(args, ",");
  }

  public static String join(String... strings) {
    return join(strings, ",");
  }

  /*Join an array of integer by  dilimeter*/
  public static String join(int[] args, String dilimeter) {
    if (args == null || args.length == 0) {
      return "";
    }

    String s = "";
    for (int i = 0; i < args.length; i++) {
      s += args[i] + dilimeter;
    }
    int pos = s.lastIndexOf(dilimeter);
    s = s.substring(0, pos).trim();
    return s;
  }
}
