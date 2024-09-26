package com.skedgo.tripkit.common.model.alert;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static com.skedgo.tripkit.common.model.realtimealert.RealtimeAlert.SEVERITY_ALERT;
import static com.skedgo.tripkit.common.model.realtimealert.RealtimeAlert.SEVERITY_WARNING;

@StringDef({SEVERITY_WARNING, SEVERITY_ALERT})
@Retention(RetentionPolicy.SOURCE)
public @interface AlertSeverity {
}