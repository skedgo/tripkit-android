package com.skedgo.tripkit

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * A base class that all Robolectric-based test classes should extend.
 * A benefit is those test classes do not need to specify the `RunWith` annotation
 * and the `Config` annotation.
 */
@RunWith(RobolectricTestRunner::class)
abstract class TripKitAndroidRobolectricTest