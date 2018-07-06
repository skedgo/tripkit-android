package com.skedgo.android.tripkit

import com.skedgo.android.common.model.Region

interface ModeFilter {

  fun execute(region: Region): List<String>?
}