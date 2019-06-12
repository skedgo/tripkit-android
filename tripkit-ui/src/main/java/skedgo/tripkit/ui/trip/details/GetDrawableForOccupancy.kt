package skedgo.tripkit.ui.trip.details

import androidx.annotation.DrawableRes
import skedgo.tripkit.routing.Occupancy
import skedgo.tripkit.ui.R

object GetDrawableForOccupancy {

  @DrawableRes
  fun execute(occupancy: Occupancy?): Int =
      when (occupancy) {
        Occupancy.Empty, Occupancy.ManySeatsAvailable -> R.drawable.occupancy_many_seats
        Occupancy.FewSeatsAvailable -> R.drawable.occupancy_few_seats
        Occupancy.StandingRoomOnly, Occupancy.CrushedStandingRoomOnly -> R.drawable.occupancy_standing_only
        Occupancy.Full -> R.drawable.ic_exclamation_white
        Occupancy.NotAcceptingPassengers -> R.drawable.ic_cross_white
        else -> R.drawable.ic_cross_white
      }
}