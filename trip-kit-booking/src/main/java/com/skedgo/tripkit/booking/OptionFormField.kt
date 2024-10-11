package com.skedgo.tripkit.booking;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class OptionFormField extends FormField {
    public static final Creator<OptionFormField> CREATOR = new Creator<OptionFormField>() {
        @Override
        public OptionFormField createFromParcel(Parcel in) {
            in.readInt();
            return new OptionFormField(in);
        }

        @Override
        public OptionFormField[] newArray(int size) {
            return new OptionFormField[size];
        }
    };
    @SerializedName("value")
    private OptionValue value;
    @SerializedName("allValues")
    private List<OptionValue> allValues;

    public OptionFormField() {
        super();
    }

    public OptionFormField(Parcel in) {
        super(in);
        this.value = in.readParcelable(OptionValue.class.getClassLoader());
        allValues = new ArrayList<>();
        in.readTypedList(allValues, OptionValue.CREATOR);
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(OPTION);
        super.writeToParcel(dest, flags);
        dest.writeParcelable(value, flags);
        dest.writeTypedList(allValues);
    }

    public static class OptionValue implements Parcelable {
        public static final Creator<OptionValue> CREATOR = new Creator<OptionValue>() {
            @Override
            public OptionValue createFromParcel(Parcel in) {
                return new OptionValue(in);
            }

            @Override
            public OptionValue[] newArray(int size) {
                return new OptionValue[size];
            }
        };
        @SerializedName("title")
        private String title;
        @SerializedName("value")
        private String value;

        public OptionValue() {
        }

        public OptionValue(Parcel in) {
            this.title = in.readString();
            this.value = in.readString();
        }

        public OptionValue(String title, String value) {
            this.title = title;
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(value);
        }

        public String getValue() {
            return value;
        }

        public String getTitle() {
            return title;
        }
    }
}