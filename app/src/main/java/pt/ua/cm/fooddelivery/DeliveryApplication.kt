package pt.ua.cm.fooddelivery

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import pt.ua.cm.fooddelivery.database.AppDatabase
import pt.ua.cm.fooddelivery.restaurant.RestaurantRepository
import timber.log.Timber

class DeliveryApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { RestaurantRepository(database.restaurantDao()) }


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}