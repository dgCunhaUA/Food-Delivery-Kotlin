package pt.ua.cm.fooddelivery.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.entities.Client

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(client: Client)

    @Query("SELECT * from client")
    fun getClient(): Flow<Client?>

    @Query("DELETE FROM client")
    suspend fun deleteAll()
}