package com.skedgo.tripkit.common.model.realtimealert

object RealtimeAlerts {
    @JvmStatic
    fun getDisplayText(alert: RealtimeAlert): String? {
        var text = alert.text()
        val title = alert.title()
        if (text != null && title != null) {
            if (text.startsWith(title)) {
                text = text.substring(title.length)
            }

            if (text.startsWith(" - ")) {
                text = text.substring(3)
            }
        }

        return text
    }
}