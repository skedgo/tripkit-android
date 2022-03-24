package com.skedgo.tripkit.booking.quickbooking

data class QuickBookResponse(
    val action: Action,
    val form: List<Form>,
    val refreshURLForSourceObject: String,
    val title: String,
    val type: String
)

data class Form(
        val fields: List<Field>,
        val title: String
)


data class Field(
        val id: String,
        val keyboardType: String,
        val method: String,
        val readOnly: Boolean,
        val refresh: Boolean,
        val sidetitle: String,
        val title: String,
        val type: String,
        val value: String
)

data class Action(
        val done: Boolean,
        val title: String
)