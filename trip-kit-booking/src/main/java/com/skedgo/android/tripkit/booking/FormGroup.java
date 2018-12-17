package com.skedgo.android.tripkit.booking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.annotation.Nullable;

public class FormGroup {

  @SerializedName("title")
  private String title;
  @SerializedName("footer")
  private String footer;
  @SerializedName("fields")
  private List<FormField> fields;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Nullable
  public String getFooter() {
    return footer;
  }

  public List<FormField> getFields() {
    return fields;
  }

  public void setFields(List<FormField> fields) {
    this.fields = fields;
  }
}
