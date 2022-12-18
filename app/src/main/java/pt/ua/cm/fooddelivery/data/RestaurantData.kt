package pt.ua.cm.fooddelivery.data

import android.content.res.Resources
import pt.ua.cm.fooddelivery.R

object RestaurantData {
    fun getRestaurantData(): ArrayList<Restaurant> {
        return arrayListOf(
            Restaurant(
                id = 1,
                nameResourceId = R.string.restaurant1,
                menus = arrayListOf(Menu(1, R.string.menu1), Menu(2, R.string.menu2)),
            ),
            Restaurant(
                id = 2,
                nameResourceId = R.string.restaurant2,
                menus = arrayListOf(Menu(1, R.string.menu1), Menu(2, R.string.menu2)),
            ),
        )
    }
}