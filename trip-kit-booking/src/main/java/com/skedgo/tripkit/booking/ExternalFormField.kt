package com.skedgo.tripkit.booking;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExternalFormField extends FormField implements Serializable {
    public static final Creator<ExternalFormField> CREATOR = new Creator<ExternalFormField>() {
        @Override
        public ExternalFormField createFromParcel(Parcel in) {
            in.readInt();
            return new ExternalFormField(in);
        }

        @Override
        public ExternalFormField[] newArray(int size) {
            return new ExternalFormField[size];
        }
    };
    @SerializedName("disregardURL")
    private String disregardURL;
    @SerializedName("nextHudText")
    private String nextHudText;
    @SerializedName("nextURL")
    private String nextURL;
    @SerializedName("value")
    private String value;

    public ExternalFormField() {
        super();
    }

    public ExternalFormField(Parcel in) {
        super(in);
        this.disregardURL = in.readString();
        this.nextHudText = in.readString();
        this.nextURL = in.readString();
        this.value = in.readString();
    }

    @Override
    public String getValue() {
        return value;
    }

    public String getDisregardURL() {
        return disregardURL;
    }

    public String getNextHudText() {
        return nextHudText;
    }

    public String getNextURL() {
        return nextURL;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(EXTERNAL);
        super.writeToParcel(dest, flags);
        dest.writeString(disregardURL);
        dest.writeString(nextHudText);
        dest.writeString(nextURL);
        dest.writeString(value);
    }
}
