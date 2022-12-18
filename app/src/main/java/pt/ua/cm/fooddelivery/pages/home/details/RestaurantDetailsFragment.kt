package pt.ua.cm.fooddelivery.pages.home.details

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.data.Restaurant
import pt.ua.cm.fooddelivery.databinding.FragmentRestaurantDetailsBinding
import pt.ua.cm.fooddelivery.pages.home.RestaurantViewModel

class RestaurantDetailsFragment : Fragment() {

    private val restaurantViewModel: RestaurantViewModel by activityViewModels()

    private lateinit var binding: FragmentRestaurantDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i("RestaurantDetailsFrag", "onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_restaurant_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.restaurantViewModel = restaurantViewModel

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        val currentRestaurant: Restaurant = restaurantViewModel.currentRestaurant.value as Restaurant

        Log.i("RestaurantDetailsFrag", "onViewCreated: $currentRestaurant.toString()")

        // Initialize the adapter and set it to the RecyclerView.
        val adapter = MenuAdapter()
        binding.menusRecycler.adapter = adapter
        adapter.submitList(currentRestaurant.menus)



        binding.testBtn.setOnClickListener {
            Log.i("wreyt,", "test")
            Log.i("wreyt", currentRestaurant.menus.toString())

            //(binding.menusRecycler.adapter as MenuAdapter).notifyDataSetChanged()
        }
    }
}