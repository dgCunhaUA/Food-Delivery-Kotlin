package pt.ua.cm.fooddelivery.network.request

import com.google.gson.annotations.SerializedName

data class OrderFinishRequest(
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
)


/*
{
    "restaurant_name": "restaurant_name",
    "restaurant_address": "universidade de aveiro portugal",
    "client_id": 1,
    "client_name": "nome",
    "client_address": "glicinias plaza aveiro portugal"
}
 */