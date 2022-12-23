package pt.ua.cm.fooddelivery.client.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "client")
data class Client (
    @SerializedName("id")
    @PrimaryKey
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