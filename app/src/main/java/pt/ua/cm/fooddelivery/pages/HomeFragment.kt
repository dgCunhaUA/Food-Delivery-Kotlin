package pt.ua.cm.fooddelivery.pages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.databinding.ActivityMainBinding
import pt.ua.cm.fooddelivery.databinding.FragmentHomeBinding
import pt.ua.cm.fooddelivery.restaurant.RestaurantAdapter
import pt.ua.cm.fooddelivery.restaurant.RestaurantModelFactory
import pt.ua.cm.fooddelivery.restaurant.RestaurantViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val restaurantViewModel: RestaurantViewModel by viewModels {
        RestaurantModelFactory((activity?.application as DeliveryApplication).restaurantRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        setRecyclerView()

        return binding.root
    }

    private fun setRecyclerView()
    {
        val mainActivity = this.activity
        if (mainActivity != null) {
            restaurantViewModel.restaurantItems.observe(mainActivity){
                binding.restaurantRecyclerView.apply {
                    layoutManager = LinearLayoutManager(activity?.applicationContext)
                    //adapter = RestaurantAdapter(it, mainActivity)
                    adapter = RestaurantAdapter(it)
                }
            }
        }
    }

}