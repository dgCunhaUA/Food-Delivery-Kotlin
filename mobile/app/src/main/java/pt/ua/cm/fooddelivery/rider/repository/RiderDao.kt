package pt.ua.cm.fooddelivery.rider.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.rider.entities.Rider

@Dao
interface RiderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rider: Rider)

    @Query("SELECT * from rider")
    fun getRider(): Flow<Rider?>

    @Query("SELECT * from rider")
    fun getAutoLoginRider(): Rider?

    @Query("SELECT * from rider")
    fun getCurrentRider(): Rider

    @Query("DELETE FROM rider")
    suspend fun deleteAll()
}