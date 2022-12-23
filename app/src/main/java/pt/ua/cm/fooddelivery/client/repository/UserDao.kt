package pt.ua.cm.fooddelivery.client.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.client.entities.Client

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(client: Client)

    @Query("SELECT * from client")
    fun getClient(): Flow<Client?>

    @Query("SELECT * from client")
    fun getAutoLoginClient(): Client?

    @Query("SELECT * from client")
    fun getCurrentClient(): Client

    @Query("DELETE FROM client")
    suspend fun deleteAll()
}