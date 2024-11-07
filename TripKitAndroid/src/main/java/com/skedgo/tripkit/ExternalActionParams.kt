package com.skedgo.tripkit

import com.skedgo.tripkit.routing.TripSegment
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style
import org.immutables.value.Value.Style.BuilderVisibility.PACKAGE
import org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE

@Immutable
@Style(visibility = PRIVATE, builderVisibility = PACKAGE)
abstract class ExternalActionParams {
    abstract fun action(): String

    abstract fun segment(): TripSegment

    abstract fun flitWaysPartnerKey(): String?

    interface Builder {
        fun action(action: String): Builder

        fun segment(segment: TripSegment?): Builder

        fun flitWaysPartnerKey(flitWaysPartnerKey: String): Builder

        fun build(): ExternalActionParams
    }

    companion object {
        fun builder(): Builder {
            return ExternalActionParamsBuilder()
        }
    }
}