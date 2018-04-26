package com.skedgo.android.tripkit

import com.skedgo.android.common.model.Region

class DefaultModeFilter : ModeFilter {
  override fun execute(region: Region): List<String>? {
    return region.transportModeIds
  }
}