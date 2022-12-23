package pt.ua.cm.fooddelivery.client.utils

import android.content.Context
import android.content.SharedPreferences
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.client.repository.UserRepository

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    private val userRepository: UserRepository = (context.applicationContext as DeliveryApplication).userRepository
        //(activity?.application as DeliveryApplication).userRepository

    companion object {
        const val USER_TOKEN = "user_token"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun getAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
}