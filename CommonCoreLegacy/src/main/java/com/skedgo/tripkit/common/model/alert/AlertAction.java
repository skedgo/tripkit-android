package com.skedgo.tripkit.common.model.alert;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.tripkit.common.model.alert.GsonAdaptersAlertAction;
import com.skedgo.tripkit.common.model.alert.ImmutableAlertAction;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersAlertAction.class)
public abstract class AlertAction implements Parcelable {

    public static final Creator<AlertAction> CREATOR = new Creator<AlertAction>() {
        public AlertAction createFromParcel(Parcel in) {
            final ArrayList<String> excludedStopCodes = in.readArrayList(ArrayList.class.getClassLoader());
            return ImmutableAlertAction.builder()
                .type(in.readString())
                .text(in.readString())
                .excludedStopCodes(excludedStopCodes)
                .build();
        }

        public AlertAction[] newArray(int size) {
            return new AlertAction[size];
        }
    };

    @Nullable
    public abstract String type();

    @Nullable
    public abstract String text();

    @Nullable
    public abstract List<String> excludedStopCodes();


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(excludedStopCodes());
        out.writeString(type());
        out.writeString(text());
    }
}