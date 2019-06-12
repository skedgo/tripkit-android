package skedgo.tripkit.ui.trip.details

import androidx.annotation.DrawableRes
import skedgo.tripkit.routing.Occupancy
import skedgo.tripkit.ui.R

object GetBackgroundResForOccupancy  {

  @DrawableRes
  fun execute(occupancy: Occupancy?): Int =
      when (occupancy) {
        Occupancy.Full -> R.drawable.occupancy_almost_full
        Occupancy.NotAcceptingPassengers -> R.drawable.occupancy_full
        else -> 0
      }
}