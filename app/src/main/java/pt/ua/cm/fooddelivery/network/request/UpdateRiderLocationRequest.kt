package pt.ua.cm.fooddelivery.network.request

import com.google.gson.annotations.SerializedName

data class UpdateRiderLocationRequest(
    @SerializedName("order_id")
    var order_id: Int,
    @SerializedName("rider_lat")
    var rider_lat: Double,
    @SerializedName("rider_lng")
    var rider_lng: Double,
)

/*
{
    "order_id": 4,
    "rider_lat": 40.6307,
    "rider_lng": -8.56399
}
 */