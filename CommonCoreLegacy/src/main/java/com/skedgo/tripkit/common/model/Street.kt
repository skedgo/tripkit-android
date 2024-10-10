package com.skedgo.tripkit.common.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Default
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@Immutable
@TypeAdapters
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersStreet::class
)
abstract class Street : Parcelable {
    abstract fun name(): String?

    abstract fun encodedWaypoints(): String?

    @Default
    open fun metres(): Float {
        return 0f
    }

    @Default
    open fun safe(): Boolean {
        return false
    }

    @Default
    open fun dismount(): Boolean {
        return false
    }

    abstract fun roadTags(): List<String?>?

    abstract fun instruction(): Instruction?

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name())
        dest.writeFloat(metres())
        dest.writeString(encodedWaypoints())
        dest.writeByte((if (safe()) 1 else 0).toByte())
        dest.writeByte((if (dismount()) 1 else 0).toByte())
        dest.writeStringList(roadTags())
    }

    enum class Instruction {
        @SerializedName("HEAD_TOWARDS")
        HEAD_TOWARDS,
        @SerializedName("CONTINUE_STRAIGHT")
        CONTINUE_STRAIGHT,
        @SerializedName("TURN_SLIGHTLY_LEFT")
        TURN_SLIGHTLY_LEFT,
        @SerializedName("TURN_LEFT")
        TURN_LEFT,
        @SerializedName("TURN_SHARPLY_LEFT")
        TURN_SHARPLY_LEFT,
        @SerializedName("TURN_SLIGHTLY_RIGHT")
        TURN_SLIGHTLY_RIGHT,
        @SerializedName("TURN_RIGHT")
        TURN_RIGHT,
        @SerializedName("TURN_SHARPLY_RIGHT")
        TURN_SHARPLY_RIGHT
    }

    companion object {
        @JvmField
        val CREATOR: Creator<Street> = object : Creator<Street> {
            override fun createFromParcel(`in`: Parcel): Street? {
                return ImmutableStreet.builder()
                    .name(`in`.readString())
                    .metres(`in`.readFloat())
                    .encodedWaypoints(`in`.readString())
                    .safe(`in`.readByte().toInt() == 1)
                    .dismount(`in`.readByte().toInt() == 1)
                    .roadTags(`in`.createStringArrayList())
                    .build()
            }

            override fun newArray(size: Int): Array<Street?> {
                return arrayOfNulls(size)
            }
        }
    }
}
