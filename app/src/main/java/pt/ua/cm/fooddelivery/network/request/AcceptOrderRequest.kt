package pt.ua.cm.fooddelivery.network.request

import com.google.gson.annotations.SerializedName


data class AcceptOrderRequest(
    @SerializedName("order_id")
    var order_id: Int,
    @SerializedName("rider_name")
    var rider_name: String,
    @SerializedName("rider_lat")
    var rider_lat: Double,
    @SerializedName("rider_lng")
    var rider_lng: Double,
    @SerializedName("order_status")
    var order_status: String,
)


/*
{
    "order_id": 3,
    "rider_name": "rider",
    "rider_lat": 0,
    "rider_lng": 0,
    "order_status": "Delivering"
}
 */