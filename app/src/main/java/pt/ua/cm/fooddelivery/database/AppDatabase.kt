package pt.ua.cm.fooddelivery.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ua.cm.fooddelivery.menu.Menu
import pt.ua.cm.fooddelivery.restaurant.Restaurant
import pt.ua.cm.fooddelivery.restaurant.RestaurantDao
import timber.log.Timber


@Database(entities = [Restaurant::class, Menu::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

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
                        populateDatabase(database.restaurantDao())
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
        private suspend fun populateDatabase(restaurantDao: RestaurantDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            Timber.i("Populating Database")

            restaurantDao.deleteAll()
            restaurantDao.insert(Restaurant(0, "Restaurant0"))
            restaurantDao.insert(Restaurant(1, "Restaurant1"))

            restaurantDao.insertMenu(Menu(0, "Menu 0", 0))
            restaurantDao.insertMenu(Menu(1, "Menu 1", 1))

        }

    }
}