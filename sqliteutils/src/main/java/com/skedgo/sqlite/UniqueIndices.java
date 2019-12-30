package com.skedgo.sqlite;

import android.text.TextUtils;

public final class UniqueIndices {
  private UniqueIndices() {}

  public static String of(String tableName, DatabaseField... fields) {
    // 'UI' stands for 'Unique Index'.
    final String indexName = tableName + "_UI_" + TextUtils.join("_", fields);
    return "CREATE UNIQUE INDEX " + indexName + " ON " + tableName + " (" + TextUtils.join(", ", fields) + ");";
  }
}