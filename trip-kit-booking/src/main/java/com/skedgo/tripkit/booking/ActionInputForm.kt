package com.skedgo.tripkit.booking

import com.google.gson.annotations.SerializedName


data class ActionInputForm(@SerializedName("input") val input: List<ActionInputFormField>)

data class ActionInputFormField(@SerializedName("field") val field: String,
                                    @SerializedName("value") val value: String)