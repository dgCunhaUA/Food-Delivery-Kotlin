package pt.ua.cm.fooddelivery.client.repository

import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.client.entities.Client

class UserRepository(private val userDao: UserDao) {

    val currentClient: Flow<Client?> = userDao.getClient()

    suspend fun insertClient(client: Client) {
        userDao.insert(client)
    }

    suspend fun deleteAll() {
        userDao.deleteAll()
    }

    fun getCurrentClient(): Client {
        return userDao.getCurrentClient()
    }

    fun getAutoLoginClient(): Client? {
        //val autoLoginClient: Client? = userDao.getAutoLoginClient()
        return userDao.getAutoLoginClient()
    }
}