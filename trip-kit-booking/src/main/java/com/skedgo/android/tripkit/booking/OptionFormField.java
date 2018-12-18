package com.skedgo.android.tripkit.booking;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class OptionFormField extends FormField {
  @SerializedName("value")
  private OptionValue value;
  @SerializedName("allValues")
  private List<OptionValue> allValues;

  public OptionFormField() {
    super();
  }

  @Override
  public OptionValue getValue() {
    return value;
  }

  public void setValue(OptionValue value) {
    this.value = value;
  }

  public List<OptionValue> getAllValues() {
    return allValues;
  }

  public void setAllValues(List<OptionValue> allValues) {
    this.allValues = allValues;
  }

  public int getSelectedIndex() {
    if (value != null && CollectionUtils.isNotEmpty(allValues)) {
      for (int i = 0; i < allValues.size(); i++) {
        if (TextUtils.equals(allValues.get(i).getValue(), value.getValue())) {
          return i;
        }
      }
    }

    return 0;
  }

  public static class OptionValue {
    @SerializedName("title")
    private String title;
    @SerializedName("value")
    private String value;

    public OptionValue(String title, String value) {
      this.title = title;
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    public String getTitle() {
      return title;
    }
  }
}