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
    GsonAdaptersProvider::class
)
abstract class Provider : Parcelable {
    abstract fun name(): String?

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name())
    }

    companion object {
        val CREATOR: Creator<Provider> = object : Creator<Provider> {
            override fun createFromParcel(`in`: Parcel): Provider? {
                return ImmutableProvider.builder()
                    .name(`in`.readString())
                    .build()
            }

            override fun newArray(size: Int): Array<Provider?> {
                return arrayOfNulls(size)
            }
        }
    }
}
