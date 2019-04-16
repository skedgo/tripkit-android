package com.skedgo.android.tripkit.booking.ui

import org.junit.runners.model.InitializationError
import org.robolectric.RobolectricTestRunner

class TestRunner @Throws(InitializationError::class) constructor(
    klass: Class<*>
) : RobolectricTestRunner(klass)
