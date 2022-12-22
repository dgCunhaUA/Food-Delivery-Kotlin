package pt.ua.cm.fooddelivery.repository

import androidx.room.*
import pt.ua.cm.fooddelivery.entities.Order
import pt.ua.cm.fooddelivery.entities.OrderWithMenus

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(order: Order)

    @Query("UPDATE menu SET orderId=:currentOrderId WHERE menuId = :menuId")
    suspend fun addMenuToCart(menuId: Int, currentOrderId: Int)

    @Query("UPDATE menu SET orderId=NULL WHERE menuId = :menuId")
    suspend fun rmMenuFromCart(menuId: Int)

    @Transaction
    @Query("SELECT * FROM `order` WHERE active=1")
    fun getCurrentCart(): OrderWithMenus

    @Query("DELETE FROM `order`")
    suspend fun deleteMenusFromCart()

    @Query("UPDATE `order` SET active=0 WHERE active=1")
    fun makeOrderInactive()
}
