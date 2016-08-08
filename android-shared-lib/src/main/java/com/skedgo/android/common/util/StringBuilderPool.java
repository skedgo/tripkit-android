package com.skedgo.android.common.util;

public class StringBuilderPool extends AbstractObjectPool<StringBuilder> {

  @Override
  protected void onRecycle(final StringBuilder obj) {
    obj.setLength(0);
  }
}