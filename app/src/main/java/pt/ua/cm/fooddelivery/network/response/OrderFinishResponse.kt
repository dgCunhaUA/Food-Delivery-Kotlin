package pt.ua.cm.fooddelivery.network.response

import com.google.gson.annotations.SerializedName

data class OrderFinishResponse(
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
    @SerializedName("order_status")
    var order_status: String,
)

/*
{
    "id": 9,
    "restaurant_name": "restaurant_name",
    "restaurant_address": "universidade de aveiro portugal",
    "client_id": 1,
    "client_name": "nome",
    "client_address": "glicinias plaza aveiro portugal",
    "order_status": "Waiting",
    "updatedAt": "2022-12-21T20:59:18.428Z",
    "createdAt": "2022-12-21T20:59:18.428Z"
}
 */