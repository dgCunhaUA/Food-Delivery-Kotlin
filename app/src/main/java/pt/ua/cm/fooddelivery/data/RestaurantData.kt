package pt.ua.cm.fooddelivery.data

import pt.ua.cm.fooddelivery.R

object RestaurantData {
    fun getRestaurantData(): ArrayList<Restaurant> {
        return arrayListOf(
            Restaurant(
                id = 1,
                nameResourceId = R.string.restaurant1,
            ),
            Restaurant(
                id = 2,
                nameResourceId = R.string.restaurant2,
            ),
        )
    }
}