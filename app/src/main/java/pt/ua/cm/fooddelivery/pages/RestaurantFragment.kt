package pt.ua.cm.fooddelivery.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import pt.ua.cm.fooddelivery.DeliveryApplication
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.databinding.FragmentHomeBinding
import pt.ua.cm.fooddelivery.databinding.FragmentRestaurantBinding
import pt.ua.cm.fooddelivery.menu.MenuAdapter
import pt.ua.cm.fooddelivery.menu.MenuModelFactory
import pt.ua.cm.fooddelivery.menu.MenuViewModel
import pt.ua.cm.fooddelivery.restaurant.RestaurantAdapter
import pt.ua.cm.fooddelivery.restaurant.RestaurantModelFactory
import pt.ua.cm.fooddelivery.restaurant.RestaurantViewModel
import pt.ua.cm.fooddelivery.restaurant.RestaurantWithMenus
import timber.log.Timber

class RestaurantFragment : Fragment() {

    private val args: RestaurantFragmentArgs by navArgs()

    private lateinit var binding: FragmentRestaurantBinding

    private val menuViewModel: MenuViewModel by viewModels {
        MenuModelFactory((activity?.application as DeliveryApplication).restaurantRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate")
        menuViewModel.getRestaurantMenus(args.id)
        Timber.i(menuViewModel.restaurantMenus.value.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("onCreateView")

        binding = FragmentRestaurantBinding.inflate(layoutInflater)

        setFields()

        return binding.root
    }

    private fun setFields()
    {
        Timber.i("setRecyclerView for restaurantId: ${args.id}")
        val mainActivity = this.activity
        if (mainActivity != null) {
            menuViewModel.restaurantMenus.observe(viewLifecycleOwner) {
                binding.restaurantName.text = it.restaurant.name

                binding.menuRecyclerView.apply {
                    layoutManager = LinearLayoutManager(activity?.applicationContext)
                    adapter = MenuAdapter(it)
                }
            }
            menuViewModel.getRestaurantMenus(args.id)
        }
    }
}