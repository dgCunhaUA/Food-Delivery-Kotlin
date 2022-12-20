package pt.ua.cm.fooddelivery.cart

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.menu.Menu

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
    suspend fun deleteAll()

    @Query("DELETE FROM `order`")
    suspend fun deleteMenusFromCart()
}
