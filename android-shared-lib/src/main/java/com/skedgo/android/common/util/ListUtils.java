package com.skedgo.android.common.util;

import android.support.annotation.NonNull;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;

public class ListUtils {
  /**
   * This was deprecated. Replacement: {@link CollectionUtils#isEmpty(java.util.Collection)}.
   */
  @Deprecated
  public static boolean isEmpty(Collection<?> list) {
    return (list == null) || list.isEmpty();
  }

  /**
   * The method name is inspired by Apache's CollectionUtils.addIgnoreNull().
   *
   * @return The parent list.
   */
  public static <T> List<T> addAllIgnoreNull(@NonNull List<T> parent,
                                             List<T> child) {
    if (child != null) {
      parent.addAll(child);
    }

    return parent;
  }
}