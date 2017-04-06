package skedgo.tripkit.analytics

fun UserInfo.toMap(): Map<String, Any> =
    mapOf<String, Any>(
        "source" to this.source as Any,
        "choiceSet" to this.choiceSet as Any
    )
