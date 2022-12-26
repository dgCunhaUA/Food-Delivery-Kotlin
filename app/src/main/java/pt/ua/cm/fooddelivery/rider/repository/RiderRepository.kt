package pt.ua.cm.fooddelivery.rider.repository

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.RiderActivity
import pt.ua.cm.fooddelivery.client.entities.Client
import pt.ua.cm.fooddelivery.client.repository.UserDao
import pt.ua.cm.fooddelivery.rider.entities.Rider

class RiderRepository(private val riderDao: RiderDao, private val application: DeliveryApplication) {

    val currentRider: Flow<Rider?> = riderDao.getRider()

    private var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)


    suspend fun insertRider(rider: Rider) {
        riderDao.insert(rider)
    }

    suspend fun deleteAll() {
        riderDao.deleteAll()
    }

    fun getAutoLoginRider(): Rider? {
        return riderDao.getAutoLoginRider()
    }

    fun getCurrentRider(): Rider {
        return riderDao.getCurrentRider()
    }

    @SuppressLint("MissingPermission")
    fun getRiderLocation(): LatLng? {
        var riderLocation: LatLng? = null

        try {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        riderLocation = LatLng(location.latitude, location.longitude)
                    }
                }
        } catch (_: Exception) { }

        return riderLocation
    }

}