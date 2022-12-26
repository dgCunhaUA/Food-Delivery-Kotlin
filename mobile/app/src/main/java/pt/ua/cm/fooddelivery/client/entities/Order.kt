package pt.ua.cm.fooddelivery.client.entities

import androidx.room.*

@Entity(tableName = "order")
data class Order(
    @PrimaryKey val orderId: Int,
    @ColumnInfo(name = "restaurantId") val restaurantId: Int,
    @ColumnInfo(name = "active") val active: Boolean,
)

/*@Entity(tableName = "orderWithMenus",
    foreignKeys = [ForeignKey(
        entity = Menu::class,
        parentColumns = ["orderId"],
        childColumns = ["orderID"],
        onDelete = ForeignKey.CASCADE)
    ])
 */
data class OrderWithMenus (
    @Embedded
    val order: Order,
    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderId",
    )
    val menus: List<Menu>
)
