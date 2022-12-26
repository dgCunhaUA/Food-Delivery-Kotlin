package pt.ua.cm.fooddelivery.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.client.entities.Restaurant
import pt.ua.cm.fooddelivery.databinding.RestaurantItemBinding

class RestaurantAdapter(
    private val restaurants: List<Restaurant>
): RecyclerView.Adapter<RestaurantViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = RestaurantItemBinding.inflate(from, parent, false)
        return RestaurantViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bindRestaurantItem(restaurants[position])
    }

    override fun getItemCount(): Int = restaurants.size
}