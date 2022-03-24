package com.skedgo.tripkit

data class TransportModeConfig(
        val defaultSelectedModesIds: List<String> = emptyList(),
        val defaultUnSelectedModesIds: List<String> = emptyList()
)
