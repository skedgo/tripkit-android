package com.skedgo.tripkit

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

/**
 * A base class that all Robolectric-based test classes should extend.
 * A benefit is those test classes do not need to specify the `RunWith` annotation
 * and the `Config` annotation.
 */
@RunWith(AndroidJUnit4::class)
abstract class TripKitAndroidRobolectricTest