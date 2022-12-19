package pt.ua.cm.fooddelivery.restaurant

import android.content.Context
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.databinding.RestaurantItemBinding
import timber.log.Timber

class RestaurantViewHolder(
    private val context: Context,
    private val binding: RestaurantItemBinding,
): RecyclerView.ViewHolder(binding.root)
{

    fun bindRestaurantItem(restaurant: Restaurant)
    {
        binding.restaurantName.text = restaurant.name

        binding.restaurantCellContainer.setOnClickListener {
            val bundle = bundleOf("id" to restaurant.restaurantId)
            it.findNavController()
                .navigate(R.id.action_homeFragment_to_restaurantFragment, bundle)
        }
    }
}