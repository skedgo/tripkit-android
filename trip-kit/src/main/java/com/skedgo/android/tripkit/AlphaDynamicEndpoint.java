package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

class AlphaDynamicEndpoint implements DynamicEndpoint {
  private String url;
  private String name;

  AlphaDynamicEndpoint(@NonNull String defaultUrl, @NonNull String defaultName) {
    this.url = defaultUrl;
    this.name = defaultName;
  }

  @Override
  public String getUrl() {
    return url + "/";
  }

  @Override
  public void setUrl(@NonNull String url) {
    this.url = url;
  }

  public String getName() {
    return name;
  }
}