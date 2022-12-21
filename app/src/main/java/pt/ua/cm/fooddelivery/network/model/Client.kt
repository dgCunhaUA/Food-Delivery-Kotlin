package pt.ua.cm.fooddelivery.network.model

import com.google.gson.annotations.SerializedName

data class Client (
    @SerializedName("id")
    var id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("address")
    var address: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("token")
    var token: String
)