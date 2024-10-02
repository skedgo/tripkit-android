package com.skedgo.tripkit.common.model.realtimealert

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.model.alert.AlertAction
import com.skedgo.tripkit.common.model.alert.AlertSeverity
import com.skedgo.tripkit.common.model.location.Location
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Default
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

/**
 * @see [RealtimeAlert](https://github.com/skedgo/skedgo-java/blob/production/RealTime/src/main/java/com/buzzhives/Realtime/RealtimeAlert.java)
 */
@Immutable
@TypeAdapters
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersRealtimeAlert::class
)
abstract class RealtimeAlert : Parcelable {
    abstract fun title(): String?

    @SerializedName("hashCode")
    abstract fun remoteHashCode(): Long

    @AlertSeverity
    abstract fun severity(): String?

    abstract fun text(): String?

    abstract fun url(): String?

    abstract fun remoteIcon(): String?

    abstract fun location(): Location?

    @Default
    open fun lastUpdated(): Long {
        return -1L
    }

    @Default
    open fun fromDate(): Long {
        return -1L
    }

    @Deprecated("")
    abstract fun serviceTripID(): String?

    @Deprecated("")
    abstract fun stopCode(): String?

    @SerializedName("action")
    abstract fun alertAction(): AlertAction?

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeString(title())
        out.writeString(text())
        out.writeString(serviceTripID())
        out.writeString(stopCode())
        out.writeLong(remoteHashCode())
        out.writeString(severity())
        out.writeParcelable(location(), 0)
        out.writeString(url())
        out.writeString(remoteIcon())
        out.writeParcelable(alertAction(), 0)
        out.writeLong(lastUpdated())
        out.writeLong(fromDate())
    }

    companion object {
        const val SEVERITY_ALERT: String = "alert"
        const val SEVERITY_WARNING: String = "warning"

        @JvmField
        val CREATOR: Creator<RealtimeAlert> = object : Creator<RealtimeAlert> {
            override fun createFromParcel(`in`: Parcel): RealtimeAlert {
                return ImmutableRealtimeAlert.builder()
                    .title(`in`.readString())
                    .text(`in`.readString())
                    .serviceTripID(`in`.readString())
                    .stopCode(`in`.readString())
                    .remoteHashCode(`in`.readLong())
                    .severity(`in`.readString())
                    .location(`in`.readParcelable<Parcelable>(Location::class.java.classLoader) as Location?)
                    .url(`in`.readString())
                    .remoteIcon(`in`.readString())
                    .alertAction(`in`.readParcelable<Parcelable>(AlertAction::class.java.classLoader) as AlertAction?)
                    .lastUpdated(`in`.readLong())
                    .fromDate(`in`.readLong())
                    .build()
            }

            override fun newArray(size: Int): Array<RealtimeAlert?> {
                return arrayOfNulls(size)
            }
        }
    }
}