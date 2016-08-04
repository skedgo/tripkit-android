/*
 * Copyright (c) SkedGo 2013
 */

package com.skedgo.android.common.util;

import android.text.TextUtils;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.StringSimilarityServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created: 21/02/13 2:57 PM
 */
public class StringUtils {
  private static final String TAG = LogUtils.makeTag(StringUtils.class);

  private static final StringSimilarityServiceImpl sStringSimilarityService
      = new StringSimilarityServiceImpl(new JaroWinklerStrategy());

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

  /**
   * This is the simplest strategy to check whether
   * a string seems to be an address, instead of an attraction
   */
  public static boolean isAnAddress(String input) {
    if (TextUtils.isEmpty(input)) {
      return false;
    }

    char firstChar = input.charAt(0);
    boolean hasNumber = firstChar <= '9' && firstChar >= '0';

    return hasNumber;
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

  public static String getDurationAsHours(long minutes) {
    if (minutes < 60) {
      return ((minutes < 10) ? "0:0" : "0:") + minutes;
    }

    final int hours = (int) Math.floor(minutes / 60);

    final long mins = minutes % 60;
    return hours + (mins < 10 ? ":0" : ":") + mins;
  }

  public static String makeArgsString(int argc) {
    String[] args = new String[argc];
    Arrays.fill(args, "?");
    String result = TextUtils.join(",", args);
    return result;
  }

  public static String[] toStringArray(int[] val) {
    String[] args = new String[val.length];
    for (int i = 0; i < val.length; i++) {
      args[i] = String.format("%d", val[i]);
    }
    return args;
  }

  public static String addParentheses(String text) {
    return "(" + text + ")";
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

  /**
   * @param strings a list of strings like: "Ronaldo", "Messi", "Ellon", "Tony"
   * @return normal written string: "Ronaldo, Messi, Ellon and Tony"
   */
  public static String joinToMakeBeautifulStrings(List<String> strings) {
    int size = strings.size();
    if (size > 1) {
      String replaceLastItem = "and " + strings.get(size - 1);
      strings.add(size - 1, replaceLastItem);
      strings.remove(strings.size() - 1);
      return join(strings, ", ");
    } else if (size == 1) {
      return strings.get(0);
    } else {
      return null;
    }
  }

  /*Join an array of strings by default dilimeter, which is comma*/
  public static String joinFromInteger(ArrayList<Integer> args) {
    if (args == null || args.size() == 0) {
      return "";
    }
    return joinFromInteger(args, ",");
  }

  /*Join an array of integer by  dilimeter*/
  public static String joinFromInteger(ArrayList<Integer> args, String dilimeter) {
    if (args == null || args.size() == 0) {
      return "";
    }

    String s = "";
    for (Integer i : args) {
      s += i + dilimeter;
    }
    int pos = s.lastIndexOf(dilimeter);
    s = s.substring(0, pos).trim();
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

  protected static int metersToFeet(int meters) {
    return (int) (meters * 3.2808399f);
  }

  protected static double round(double d) {
    return Math.round(d * 100) / 100.0;
  }

  /**
   * @return A value between 0.0 & 1.0 indicating how similar the strings are
   * <p/>
   * 0.0 indicates there is no similarity at all whereas 1.0 means there is an exact match
   */
  public static double similarity(String first, String second) {
    return sStringSimilarityService.score(first, second);
  }

  public static String cleanse(String str) {
    if (!TextUtils.isEmpty(str)) {
      str = str.replaceAll("\\n", " ");
      str = str.replaceAll("\\r", " ");
      str = str.replaceAll("\\t", " ");
    }

    return str;
  }

}
