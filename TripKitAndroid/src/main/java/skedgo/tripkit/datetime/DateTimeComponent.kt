package skedgo.tripkit.datetime

import dagger.Subcomponent
import javax.inject.Singleton

/**
 * Creates UseCases and Repositories related to the DateTime feature.
 */
@Singleton
@Subcomponent(modules = arrayOf(
    DateTimeDataModule::class
))
interface DateTimeComponent {
  fun printTime(): PrintTime
}