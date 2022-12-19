package pt.ua.cm.fooddelivery.cart

import androidx.room.*
import pt.ua.cm.fooddelivery.menu.Menu

@Entity(tableName = "order")
data class Order(
    @PrimaryKey val orderId: Int,
    @ColumnInfo(name = "restaurantId") val restaurantId: Int,
    @ColumnInfo(name = "active") val active: Boolean,
)

data class OrderWithMenus (
    @Embedded
    val order: Order,
    @Relation(
        parentColumn = "restaurantId",
        entityColumn = "restaurantId"
    )
    val menus: List<Menu>
)
