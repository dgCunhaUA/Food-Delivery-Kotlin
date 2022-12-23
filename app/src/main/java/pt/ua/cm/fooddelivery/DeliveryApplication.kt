package pt.ua.cm.fooddelivery

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import pt.ua.cm.fooddelivery.client.repository.OrderRepository
import pt.ua.cm.fooddelivery.client.repository.RestaurantRepository
import pt.ua.cm.fooddelivery.client.repository.UserRepository
import pt.ua.cm.fooddelivery.database.AppDatabase
import timber.log.Timber

class DeliveryApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val restaurantRepository by lazy {
        RestaurantRepository(database.restaurantDao())
    }

    val orderRepository by lazy {
        OrderRepository(database.orderDao())
    }

    val userRepository by lazy {
        UserRepository(database.userDao())
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}