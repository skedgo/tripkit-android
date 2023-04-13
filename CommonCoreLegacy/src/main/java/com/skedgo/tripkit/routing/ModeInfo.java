package com.skedgo.tripkit.routing;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * @see <a href="http://skedgo.github.io/tripgo-api/site/faq/#mode-identifiers">Mode Identifiers</a>
 */
// FIXME let's remove Parcelable and maybe migrate to a data class
public class ModeInfo implements Parcelable {
    public static final Creator<ModeInfo> CREATOR = new Creator<ModeInfo>() {
        @Override
        public ModeInfo createFromParcel(Parcel source) {
            return new ModeInfo(source);
        }

        @Override
        public ModeInfo[] newArray(int size) {
            return new ModeInfo[0];
        }
    };

    public static final float MAP_LIST_SIZE_RATIO = 1f;

    @SerializedName("alt")
    private String alternativeText;
    @SerializedName("localIcon")
    private String localIconName;
    @SerializedName("remoteIcon")
    private String remoteIconName;
    @SerializedName("remoteDarkIcon")
    private String remoteDarkIconName;
    @SerializedName("description")
    private String description;
    @SerializedName("identifier")
    private String id;
    @SerializedName("color")
    private ServiceColor color;
    @SerializedName("remoteIconIsTemplate")
    private boolean remoteIconIsTemplate;
    @SerializedName("remoteIconIsBranding")
    private boolean remoteIconIsBranding;


    public ModeInfo() {
    }

    private ModeInfo(@NonNull Parcel source) {
        alternativeText = source.readString();
        localIconName = source.readString();
        remoteIconName = source.readString();
        remoteDarkIconName = source.readString();
        description = source.readString();
        id = source.readString();
        color = source.readParcelable(ServiceColor.class.getClassLoader());
        remoteIconIsTemplate = source.readInt() == 1;
        remoteIconIsBranding = source.readInt() == 0;
    }

    /**
     * Indicates a human-readable name of the transport (e.g, "Train").
     */
    @NonNull
    public String getAlternativeText() {
        return alternativeText;
    }

    public void setAlternativeText(String alternativeText) {
        this.alternativeText = alternativeText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(alternativeText);
        dest.writeString(localIconName);
        dest.writeString(remoteIconName);
        dest.writeString(remoteDarkIconName);
        dest.writeString(description);
        dest.writeString(id);
        dest.writeParcelable(color, 0);
        dest.writeInt(remoteIconIsTemplate ? 1 : 0);
        dest.writeInt(remoteIconIsBranding ? 1 : 0);
    }

    public boolean getRemoteIconIsTemplate() {
        return remoteIconIsTemplate;
    }

    public void setRemoteIconIsTemplate(boolean remoteIconIsTemplate) {
        this.remoteIconIsTemplate = remoteIconIsTemplate;
    }

    public boolean getRemoteIconIsBranding() {
        return remoteIconIsBranding;
    }

    public void setRemoteIconIsBranding(boolean remoteIconIsBranding) {
        this.remoteIconIsBranding = remoteIconIsBranding;
    }

    public String getLocalIconName() {
        return localIconName;
    }

    public void setLocalIconName(String localIconName) {
        this.localIconName = localIconName;
    }

    public String getRemoteIconName() {
        return remoteIconName;
    }

    public void setRemoteIconName(String remoteIconName) {
        this.remoteIconName = remoteIconName;
    }

    @Nullable
    public String getRemoteDarkIconName() {
        return remoteDarkIconName;
    }

    public void setRemoteDarkIconName(String remoteDarkIconName) {
        this.remoteDarkIconName = remoteDarkIconName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VehicleMode getModeCompat() {
        return VehicleMode.from(localIconName);
    }

    /**
     * @see <a href="http://skedgo.github.io/tripgo-api/site/faq/#mode-identifiers">Mode Identifiers</a>
     */
    @Nullable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nullable
    public ServiceColor getColor() {
        return color;
    }

    public void setColor(ServiceColor color) {
        this.color = color;
    }
}