package com.skedgo.tripkit.booking;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

public class QrFormField extends FormField {
    public static final Creator<QrFormField> CREATOR = new Creator<QrFormField>() {
        @Override
        public QrFormField createFromParcel(Parcel in) {
            in.readInt();
            return new QrFormField(in);
        }

        @Override
        public QrFormField[] newArray(int size) {
            return new QrFormField[size];
        }
    };

    @SerializedName("value")
    private String value;

    public QrFormField(Parcel in) {
        super(in);
        this.value = in.readString();
    }

    public QrFormField() {
        super();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(QRCODE);
        super.writeToParcel(dest, flags);
        dest.writeString(value);
    }

    @Nullable
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
