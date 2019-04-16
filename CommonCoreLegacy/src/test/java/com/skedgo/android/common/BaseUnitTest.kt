package com.skedgo.android.common

import org.junit.runner.RunWith

/**
 * A base class that all Robolectric-based test classes should extend.
 * A benefit is those test classes do not need to specify the `RunWith` annotation
 * and the `Config` annotation.
 */
@RunWith(TestRunner::class)
abstract class BaseUnitTest