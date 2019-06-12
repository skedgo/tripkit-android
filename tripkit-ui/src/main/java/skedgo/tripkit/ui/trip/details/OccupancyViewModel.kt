package skedgo.tripkit.ui.trip.details

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.databinding.*
import me.tatarka.bindingcollectionadapter2.ItemBinding
import skedgo.tripkit.routing.*
import skedgo.tripkit.ui.BR
import skedgo.tripkit.ui.R
import javax.inject.Inject

open class OccupancyViewModel @Inject constructor(private val context: Context) {
  // For bus occupancy
  val drawableLeft = ObservableField<Drawable>()
  val occupancyText = ObservableField<String>()
  val hasOccupancySingleInformation = ObservableBoolean(false)
  val textColor = ObservableInt()
  val background = ObservableField<Drawable?>()

  // For train occupancy
  val hasOccupancyInformation = ObservableBoolean(false)
  val itemBinding = ItemBinding.of<TrainOccupancyItemViewModel>(BR.occupancy, R.layout.train_item)
  val items: ObservableList<TrainOccupancyItemViewModel> = ObservableArrayList()

  open fun setOccupancy(vehicle: RealTimeVehicle, showAverage: Boolean) {
    hasOccupancyInformation.set(vehicle.hasVehiclesOccupancy())
    val hasOccupancyInfo = vehicle.components.orEmpty().sumBy { it.size } > 0
    hasOccupancySingleInformation.set(hasOccupancyInfo && (vehicle.hasSingleVehicleOccupancy() || showAverage))
    if (hasOccupancyInfo) {
      if (vehicle.hasSingleVehicleOccupancy() || showAverage) {
        showOccupancyForOneComponentVehicle(vehicle.getAverageOccupancy()!!)
      }
    }

    if (vehicle.hasVehiclesOccupancy()) {
      showOccupancyForManyComponentsVehicle(vehicle.components!!)
    }
  }

  private fun showOccupancyForManyComponentsVehicle(vehicleComponents: List<List<VehicleComponent>>) {
    vehicleComponents
        .map {
          it.mapIndexed { index: Int, vehicleComponent: VehicleComponent ->
            TrainOccupancyItemViewModel(
                ContextCompat.getColor(context, GetColorForOccupancy.execute(vehicleComponent.getOccupancy())),
                ContextCompat.getDrawable(context,
                    when (index) {
                      it.lastIndex -> R.drawable.ic_train_head
                      else -> R.drawable.ic_train_carriage
                    })
            )
          }
        }
        .flatten()
        .let {
          items.clear()
          items.addAll(it)
        }
  }

  private fun showOccupancyForOneComponentVehicle(occupancy: Occupancy) {
    occupancyText.set(context.getString(GetTextForOccupancy.execute(occupancy)).toUpperCase())
    drawableLeft.set(ContextCompat.getDrawable(context, GetDrawableForOccupancy.execute(occupancy)))
    textColor.set(ContextCompat.getColor(context, GetColorForOccupancy.execute(occupancy)))
    val backgroundRes = GetBackgroundResForOccupancy.execute(occupancy)
    background.set(if (backgroundRes == 0) null else ContextCompat.getDrawable(context, backgroundRes))
  }

  fun hasInformation(): Boolean = hasOccupancyInformation.get() || hasOccupancySingleInformation.get()
}
