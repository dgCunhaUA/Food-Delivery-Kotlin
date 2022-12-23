package pt.ua.cm.fooddelivery.rider.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.client.viewmodel.CartModelFactory
import pt.ua.cm.fooddelivery.client.viewmodel.CartViewModel
import pt.ua.cm.fooddelivery.rider.viewmodel.RiderHomeModelFactory
import pt.ua.cm.fooddelivery.rider.viewmodel.RiderHomeViewModel
import timber.log.Timber

class RiderHomeFragment : Fragment() {

    private val homeViewModel: RiderHomeViewModel by viewModels {
        RiderHomeModelFactory(
            (activity?.application as DeliveryApplication).riderRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*homeViewModel.currentRider.observe(this) {
            Timber.i("RIDER: $it")
        }

         */
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rider_home, container, false)
    }


}