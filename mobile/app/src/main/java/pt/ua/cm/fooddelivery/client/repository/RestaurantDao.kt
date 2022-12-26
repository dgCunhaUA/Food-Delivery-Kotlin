package pt.ua.cm.fooddelivery.client.repository

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.client.entities.Menu
import pt.ua.cm.fooddelivery.client.entities.Restaurant
import pt.ua.cm.fooddelivery.client.entities.RestaurantWithMenus

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM restaurant")
    fun getAll(): Flow<List<Restaurant>>

    @Query("SELECT * FROM restaurant WHERE restaurantId = :restaurantId")
    fun getRestaurantById(restaurantId: Int): Restaurant

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurant: Restaurant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menu: Menu)

    @Transaction
    @Query("SELECT * FROM restaurant WHERE restaurantId = :restaurantId")
    fun getRestaurantWithMenus(restaurantId: Int): RestaurantWithMenus

    @Query("DELETE FROM restaurant")
    suspend fun deleteAll()

}