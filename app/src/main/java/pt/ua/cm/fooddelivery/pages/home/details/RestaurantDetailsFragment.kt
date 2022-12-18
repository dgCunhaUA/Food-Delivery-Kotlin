package pt.ua.cm.fooddelivery.pages.home.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.data.Menu
import pt.ua.cm.fooddelivery.data.Restaurant
import pt.ua.cm.fooddelivery.databinding.FragmentRestaurantDetailsBinding
import pt.ua.cm.fooddelivery.pages.home.RestaurantAdapter
import pt.ua.cm.fooddelivery.pages.home.RestaurantViewModel

class RestaurantDetailsFragment : Fragment() {

    private val restaurantViewModel: RestaurantViewModel by activityViewModels()

    private lateinit var binding: FragmentRestaurantDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        // Initialize the adapter and set it to the RecyclerView.
        val adapter = MenusAdapter()
        binding.menusRecycler.adapter = adapter
        adapter.submitList(currentRestaurant.menus)

    }
}