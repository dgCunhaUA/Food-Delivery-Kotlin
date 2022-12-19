package pt.ua.cm.fooddelivery.restaurant

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import pt.ua.cm.fooddelivery.menu.Menu


@Dao
interface RestaurantWithMenusDao {

    @Transaction
    @Query("SELECT * FROM restaurant")
    suspend fun getAllRestaurantsWithMenus(): List<Menu>
}