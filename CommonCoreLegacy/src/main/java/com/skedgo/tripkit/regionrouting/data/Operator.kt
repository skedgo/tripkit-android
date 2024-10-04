package com.skedgo.tripkit.regionrouting.data

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@Immutable
@TypeAdapters
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersOperator::class
)
abstract class Operator : Parcelable {
    abstract fun id(): String?

    abstract fun name(): String?


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeString(id())
        out.writeString(name())
    }

    companion object {
        val CREATOR: Creator<Operator> = object : Creator<Operator> {
            override fun createFromParcel(`in`: Parcel): Operator? {
                return ImmutableOperator.builder()
                    .id(`in`.readString())
                    .name(`in`.readString())
                    .build()
            }

            override fun newArray(size: Int): Array<Operator?> {
                return arrayOfNulls(size)
            }
        }
    }
}