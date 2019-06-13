package skedgo.tripkit.time

import org.joda.time.DateTime
import javax.inject.Inject

/**
 * In most cases, we should use this UseCase
 * in place of [System.currentTimeMillis]
 * to make the code more testable.
 */
open class GetNow @Inject internal constructor() {
  open fun execute(): DateTime = DateTime.now()
}
