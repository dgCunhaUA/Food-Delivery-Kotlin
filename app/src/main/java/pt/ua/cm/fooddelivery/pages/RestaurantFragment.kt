package pt.ua.cm.fooddelivery.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.databinding.FragmentHomeBinding
import pt.ua.cm.fooddelivery.databinding.FragmentRestaurantBinding

class RestaurantFragment : Fragment() {

    private val args: RestaurantFragmentArgs by navArgs()

    private lateinit var binding: FragmentRestaurantBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_restaurant, container, false)
        binding = FragmentRestaurantBinding.inflate(layoutInflater)

        binding.restaurantName.text = args.id.toString()

        return binding.root
    }

}