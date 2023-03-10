package pt.ua.cm.fooddelivery.client.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.client.adapter.RestaurantAdapter
import pt.ua.cm.fooddelivery.client.viewmodel.RestaurantModelFactory
import pt.ua.cm.fooddelivery.client.viewmodel.RestaurantViewModel
import pt.ua.cm.fooddelivery.databinding.FragmentHomeBinding

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
                    adapter = RestaurantAdapter(it)
                }
            }
        }
    }

}