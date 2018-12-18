package com.skedgo.android.tripkit.booking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class InputForm {
  @SerializedName("input")
  private List<FormField> input;

  // Collecting user data
  public static InputForm from(List<FormGroup> formGroups) {
    if (formGroups == null) {
      return null;
    }

    List<FormField> bookingItems = new ArrayList<>();
    for (FormGroup formGroup : formGroups) {
      for (FormField item : formGroup.getFields()) {
        if (!item.isReadOnly()) {
          bookingItems.add(item);
        }
      }
    }

    // Create new post data object
    InputForm input = new InputForm();
    input.input = bookingItems;
    return input;
  }

  public List<FormField> input() {
    return input;
  }
}