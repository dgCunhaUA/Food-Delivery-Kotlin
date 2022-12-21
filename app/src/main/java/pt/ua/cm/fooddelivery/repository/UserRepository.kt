package pt.ua.cm.fooddelivery.repository

import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.entities.Client
import pt.ua.cm.fooddelivery.entities.Restaurant
import pt.ua.cm.fooddelivery.network.Api
import pt.ua.cm.fooddelivery.network.request.LoginRequest
import retrofit2.Response

class UserRepository(private val userDao: UserDao) {

    val currentClient: Flow<Client?> = userDao.getClient()

    suspend fun insertClient(client: Client) {
        userDao.insert(client)
    }

    suspend fun deleteAll() {
        userDao.deleteAll()
    }
}