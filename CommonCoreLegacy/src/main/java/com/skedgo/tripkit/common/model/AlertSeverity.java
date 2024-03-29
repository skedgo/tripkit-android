package com.skedgo.tripkit.common.model;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.skedgo.tripkit.common.model.RealtimeAlert.SEVERITY_ALERT;
import static com.skedgo.tripkit.common.model.RealtimeAlert.SEVERITY_WARNING;

@StringDef({SEVERITY_WARNING, SEVERITY_ALERT})
@Retention(RetentionPolicy.SOURCE)
public @interface AlertSeverity {}