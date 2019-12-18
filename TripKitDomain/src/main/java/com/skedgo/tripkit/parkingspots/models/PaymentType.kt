package com.skedgo.tripkit.parkingspots.models

enum class PaymentType(val value: String) {

  Meter("METER"),
  CreditCard("CREDIT_CARD"),
  Phone("PHONE"),
  Coins("COINS")
}