package pt.ua.cm.fooddelivery.menu

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "menu")
data class Menu(
    @PrimaryKey val menuId: Int,
    @ColumnInfo(name = "name") val name: String,
    val restaurantId: Int,
)
