package pt.ua.cm.fooddelivery.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.menu.Menu
import pt.ua.cm.fooddelivery.cart.Order
import pt.ua.cm.fooddelivery.cart.OrderDao
import pt.ua.cm.fooddelivery.restaurant.Restaurant
import pt.ua.cm.fooddelivery.restaurant.RestaurantDao
import timber.log.Timber


@Database(entities = [Restaurant::class, Menu::class, Order::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao
    abstract fun orderDao(): OrderDao

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
                        populateDatabase(database.restaurantDao(), database.orderDao())
                    }
                }

                // return instance
                instance
            }
        }


        /*private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {

            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                Log.i("AppDatabase", "onCreate")

                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.restaurantDao())
                    }
                }
            }
        }

         */

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

            val menu0 = Menu(0, "Menu 0", 0)
            val menu1 = Menu(1, "Menu 1", 0)
            val menu2 = Menu(2, "Menu 2", 1)

            val order0 = Order(0, 0, true)


            restaurantDao.deleteAll()
            restaurantDao.insert(restaurant0)
            restaurantDao.insert(restaurant1)
            restaurantDao.insert(restaurant2)

            restaurantDao.insertMenu(menu0)
            restaurantDao.insertMenu(menu1)
            restaurantDao.insertMenu(menu2)


            orderDao.deleteAll()
            orderDao.insert(order0)

            orderDao.insertMenu(menu0)
            orderDao.insertMenu(menu1)

        }
    }
}