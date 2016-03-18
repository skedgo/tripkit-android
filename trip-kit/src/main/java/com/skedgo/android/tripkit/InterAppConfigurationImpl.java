package com.skedgo.android.tripkit;

public class InterAppConfigurationImpl implements InterAppConfiguration {

  private String flitwaysPartnerkey;

  @Override public String getFlitwaysPartnerkey() {
    return this.flitwaysPartnerkey;
  }

  @Override public void setFlitwaysPartnerkey(String flitwaysPartnerkey) {
    this.flitwaysPartnerkey = flitwaysPartnerkey;
  }
}