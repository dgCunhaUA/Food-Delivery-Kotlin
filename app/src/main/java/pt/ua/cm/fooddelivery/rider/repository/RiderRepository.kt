package pt.ua.cm.fooddelivery.rider.repository

import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.client.repository.UserDao
import pt.ua.cm.fooddelivery.rider.entities.Rider

class RiderRepository(private val riderDao: RiderDao) {

    val currentRider: Flow<Rider?> = riderDao.getRider()

    suspend fun insertRider(rider: Rider) {
        riderDao.insert(rider)
    }

    fun getAutoLoginRider(): Rider? {
        return riderDao.getAutoLoginRider()
    }
}