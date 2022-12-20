package pt.ua.cm.fooddelivery.entities

import androidx.room.*

@Entity(tableName = "restaurant")
data class Restaurant(
    @PrimaryKey val restaurantId: Int,
    @ColumnInfo(name = "name") val name: String,
)

data class RestaurantWithMenus(
    @Embedded
    val restaurant: Restaurant,
    @Relation(
        parentColumn = "restaurantId",
        entityColumn = "restaurantId"
    )
    val menus: List<Menu>
)