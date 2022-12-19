package pt.ua.cm.fooddelivery.restaurant

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM restaurant")
    fun getAll(): Flow<List<Restaurant>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(restaurant: Restaurant)

    @Query("DELETE FROM restaurant")
    suspend fun deleteAll()

}