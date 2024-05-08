package com.skedgo.tripkit.routing

import androidx.annotation.StringDef

@Retention(AnnotationRetention.RUNTIME)
@StringDef(
    RemoteIcon.NEURON,
    RemoteIcon.LIME,
)
annotation class RemoteIcon {
    companion object {
        const val NEURON = "neuron"
        const val LIME = "lime"
    }
}