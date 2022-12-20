package pt.ua.cm.fooddelivery.repository

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.entities.Restaurant
import pt.ua.cm.fooddelivery.entities.RestaurantWithMenus
import pt.ua.cm.fooddelivery.entities.Menu

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM restaurant")
    fun getAll(): Flow<List<Restaurant>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurant: Restaurant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menu: Menu)

    @Transaction
    @Query("SELECT * FROM restaurant WHERE restaurantId = :restaurantId")
    fun getRestaurantWithMenus(restaurantId: Int): RestaurantWithMenus
    //suspend fun getRestaurantWithMenus(restaurantId: Int): RestaurantWithMenus

    @Query("DELETE FROM restaurant")
    suspend fun deleteAll()

}