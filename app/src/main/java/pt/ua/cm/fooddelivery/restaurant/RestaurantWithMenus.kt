package pt.ua.cm.fooddelivery.restaurant

import androidx.room.Embedded
import androidx.room.Relation
import pt.ua.cm.fooddelivery.menu.Menu

data class RestaurantWithMenus(

    @Embedded
    val restaurant: Restaurant,
    @Relation(
        parentColumn = "restaurantId",
        entityColumn = "restaurantId"
    )
    val menus: List<Menu>
) 