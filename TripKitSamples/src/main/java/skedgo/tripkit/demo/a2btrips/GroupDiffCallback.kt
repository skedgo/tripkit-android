package skedgo.tripkit.demo.a2btrips

import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList

object GroupDiffCallback : DiffObservableList.Callback<TripViewModel> {
  override fun areItemsTheSame(lhs: TripViewModel?, rhs: TripViewModel?): Boolean
      = lhs!!.group.uuid() == rhs!!.group.uuid()

  override fun areContentsTheSame(lhs: TripViewModel?, rhs: TripViewModel?): Boolean
      = lhs!!.group.uuid() == rhs!!.group.uuid()
}
