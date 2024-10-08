package com.skedgo.tripkit.common.model.alert

import androidx.annotation.StringDef
import com.skedgo.tripkit.common.model.realtimealert.RealtimeAlert
import kotlin.annotation.AnnotationRetention.SOURCE

@StringDef(RealtimeAlert.SEVERITY_WARNING, RealtimeAlert.SEVERITY_ALERT)
@Retention(SOURCE)
annotation class AlertSeverity 