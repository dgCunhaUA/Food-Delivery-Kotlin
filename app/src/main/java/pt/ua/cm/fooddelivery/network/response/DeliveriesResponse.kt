package pt.ua.cm.fooddelivery.network.response

import com.google.gson.annotations.SerializedName

data class DeliveriesResponse(
    @SerializedName("id")
    var id: Int,
    @SerializedName("restaurant_name")
    var restaurant_name: String,
    @SerializedName("restaurant_address")
    var restaurant_address: String,
    @SerializedName("client_id")
    var client_id: Int,
    @SerializedName("client_name")
    var client_name: String,
    @SerializedName("client_address")
    var client_address: String,
    @SerializedName("rider_name")
    var rider_name: String?,
    @SerializedName("rider_lat")
    var rider_lat: Double?,
    @SerializedName("rider_lng")
    var rider_lng: Double?,
    @SerializedName("order_status")
    var order_status: String,
)


/*
[
    {
        "id": 8,
        "restaurant_name": "Guaros Burguer Bar",
        "restaurant_address": "R. Dr. Barbosa de Magalhães 4, 3800-200 Aveiro",
        "client_id": 1,
        "client_name": "name",
        "client_address": "add",
        "rider_name": "rider",
        "rider_lat": 40.6414,
        "rider_lng": -8.65516,
        "order_status": "Delivering",
        "createdAt": "2022-11-07T17:51:41.000Z",
        "updatedAt": "2022-11-07T20:00:36.000Z"
    },


 */