package pt.ua.cm.fooddelivery.entities

import androidx.room.*

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
        parentColumn = "orderId",
        entityColumn = "orderId",
    )
    val menus: List<Menu>
)
