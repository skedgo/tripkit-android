package skedgo.tripkit.ui.trip.details

import androidx.annotation.ColorRes
import skedgo.tripkit.routing.Occupancy
import skedgo.tripkit.ui.R

object GetColorForOccupancy {

  @ColorRes
  fun execute(occupancy: Occupancy?): Int =
      when (occupancy) {
        Occupancy.Empty, Occupancy.ManySeatsAvailable -> R.color.occupancyManySeats
        Occupancy.FewSeatsAvailable -> R.color.occupancyFewSeats
        Occupancy.StandingRoomOnly, Occupancy.CrushedStandingRoomOnly -> R.color.occupancyStandingOnly
        Occupancy.Full -> android.R.color.white
        Occupancy.NotAcceptingPassengers -> android.R.color.white
        else -> android.R.color.white
      }
}