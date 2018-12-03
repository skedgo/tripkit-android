package skedgo.tripkit.routing

fun Occupancy.toVehicleComponentLists(): List<List<VehicleComponent>> =
    listOf(listOf(
        ImmutableVehicleComponent.builder()
            .occupancy(this)
            .build())
    )