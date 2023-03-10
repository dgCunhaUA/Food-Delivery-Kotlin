package pt.ua.cm.fooddelivery.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.client.entities.Menu
import pt.ua.cm.fooddelivery.client.entities.Order
import pt.ua.cm.fooddelivery.client.entities.Restaurant
import pt.ua.cm.fooddelivery.client.repository.OrderDao
import pt.ua.cm.fooddelivery.client.repository.RestaurantDao
import pt.ua.cm.fooddelivery.client.repository.UserDao
import pt.ua.cm.fooddelivery.rider.entities.Rider
import pt.ua.cm.fooddelivery.rider.repository.RiderDao
import timber.log.Timber

@Database(entities = [Restaurant::class, Menu::class, Order::class, Client::class, Rider::class], version = 12, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao
    abstract fun orderDao(): OrderDao
    abstract fun userDao(): UserDao
    abstract fun riderDao(): RiderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    //.addCallback(AppDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        //instance.clearAllTables()
                        populateDatabase(database.restaurantDao(), database.orderDao())
                    }
                }

                // return instance
                instance
            }
        }

        /**
         * Populate the database in a new coroutine.
         */
        private suspend fun populateDatabase(restaurantDao: RestaurantDao, orderDao: OrderDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            Timber.i("Populating Database")

            val restaurant0 = Restaurant(0, "Restaurant0")
            val restaurant1 = Restaurant(1, "Restaurant1")
            val restaurant2 = Restaurant(2, "Restaurant2")

            val menu0 = Menu(0, "Menu 0", 0, null)
            val menu1 = Menu(1, "Menu 1", 0, null)
            val menu2 = Menu(2, "Menu 2", 1, null)

            val order0 = Order(0, 0, true)


            restaurantDao.insert(restaurant0)
            restaurantDao.insert(restaurant1)
            restaurantDao.insert(restaurant2)

            restaurantDao.insertMenu(menu0)
            restaurantDao.insertMenu(menu1)
            restaurantDao.insertMenu(menu2)


            orderDao.insert(order0)

        }
    }
}