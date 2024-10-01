package com.skedgo.tripkit.routing

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@TypeAdapters
@Immutable
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersSource::class
)
abstract class Source : Parcelable {
    abstract fun disclaimer(): String?

    abstract fun provider(): Provider?

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(disclaimer())
        dest.writeParcelable(provider(), flags)
    }

    companion object {
        val CREATOR: Creator<Source> = object : Creator<Source> {
            override fun createFromParcel(`in`: Parcel): Source {
                return ImmutableSource.builder()
                    .disclaimer(`in`.readString())
                    .provider(`in`.readParcelable<Parcelable>(Provider::class.java.classLoader) as Provider?)
                    .build()
            }

            override fun newArray(size: Int): Array<Source?> {
                return arrayOfNulls(size)
            }
        }
    }
}
