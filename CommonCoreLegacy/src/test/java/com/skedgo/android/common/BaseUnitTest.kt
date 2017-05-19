package com.skedgo.android.common

import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * A base class that all Robolectric-based test classes should extend.
 * A benefit is those test classes do not need to specify the `RunWith` annotation
 * and the `Config` annotation.
 */
@RunWith(TestRunner::class)
@Config(constants = BuildConfig::class)
abstract class BaseUnitTest