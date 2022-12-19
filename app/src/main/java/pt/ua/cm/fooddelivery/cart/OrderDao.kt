package pt.ua.cm.fooddelivery.cart

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.menu.Menu

@Dao
interface OrderDao {

    //@Query("SELECT * FROM `order` WHERE active=1")
    //fun getCartOrder(): Flow<Order>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(order: Order)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menu: Menu)

    @Transaction
    @Query("SELECT * FROM `order` WHERE active=1")
    fun getCartWithMenus(): OrderWithMenus

    @Query("DELETE FROM `order`")
    suspend fun deleteAll()
}
