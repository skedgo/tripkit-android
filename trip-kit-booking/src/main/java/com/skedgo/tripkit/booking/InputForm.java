package com.skedgo.tripkit.booking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class InputForm implements Parcelable {
    public static final Parcelable.Creator<InputForm> CREATOR = new Parcelable.Creator<InputForm>() {
        public InputForm createFromParcel(Parcel in) {
            return new InputForm(in);
        }

        public InputForm[] newArray(int size) {
            return new InputForm[size];
        }
    };
    @SerializedName("input")
    private List<FormField> input;

    public InputForm() {
    }

    private InputForm(Parcel in) {
        input = new ArrayList<>();
        in.readTypedList(input, FormField.CREATOR);
    }

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

        return InputForm.fromFields(bookingItems);
    }

    public static InputForm fromFields(List<FormField> bookingItems) {
        // Create new post data object
        InputForm input = new InputForm();
        input.input = bookingItems;
        return input;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeTypedList(input);
    }

    public List<FormField> input() {
        return input;
    }
}