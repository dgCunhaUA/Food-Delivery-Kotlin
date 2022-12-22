package pt.ua.cm.fooddelivery.repository

import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.entities.Client

class UserRepository(private val userDao: UserDao) {

    val currentClient: Flow<Client?> = userDao.getClient()

    suspend fun insertClient(client: Client) {
        userDao.insert(client)
    }

    suspend fun deleteAll() {
        userDao.deleteAll()
    }

    suspend fun getCurrentClient(): Client {
        return userDao.getCurrentClient()
    }
}