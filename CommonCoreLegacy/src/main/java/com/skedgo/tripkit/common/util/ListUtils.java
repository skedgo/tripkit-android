package com.skedgo.tripkit.common.util;

import java.util.Collection;

@Deprecated
public final class ListUtils {
  private ListUtils() {}

  /**
   * This will be removed when we finish migrating its usages to Kotlin.
   */
  @Deprecated
  public static boolean isEmpty(Collection<?> list) {
    return (list == null) || list.isEmpty();
  }
}
