package pt.ua.cm.fooddelivery.client.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "menu")
data class Menu(
    @PrimaryKey val menuId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "restaurantId") val restaurantId: Int,
    @ColumnInfo(name = "orderId") val orderId: Int?,
)
