package pt.ua.cm.fooddelivery.rider.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rider")
data class Rider (

    @SerializedName("id"        )@PrimaryKey var id        : Int,
    @SerializedName("name"      ) var name      : String,
    @SerializedName("address"   ) var address   : String? = null,
    @SerializedName("email"     ) var email     : String? = null,
    @SerializedName("password"  ) var password  : String? = null,
    @SerializedName("vehicle"   ) var vehicle   : String? = null,
    @SerializedName("token"     ) var token     : String? = null,
    @SerializedName("photo"     ) var photo     : String? = null,

)