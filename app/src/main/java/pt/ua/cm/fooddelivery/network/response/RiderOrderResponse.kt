package pt.ua.cm.fooddelivery.network.response

import com.google.gson.annotations.SerializedName

data class RiderOrderResponse (
    @SerializedName("id"                 ) var id                : Int?    = null,
    @SerializedName("restaurant_name"    ) var restaurantName    : String? = null,
    @SerializedName("restaurant_address" ) var restaurantAddress : String? = null,
    @SerializedName("client_id"          ) var clientId          : Int?    = null,
    @SerializedName("client_name"        ) var clientName        : String? = null,
    @SerializedName("client_address"     ) var clientAddress     : String? = null,
    @SerializedName("rider_name"         ) var riderName         : String? = null,
    @SerializedName("rider_lat"          ) var riderLat          : Double? = null,
    @SerializedName("rider_lng"          ) var riderLng          : Double? = null,
    @SerializedName("order_status"       ) var orderStatus       : String? = null,
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
    }
]
 */