package pt.ua.cm.fooddelivery.restaurant

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.R
import pt.ua.cm.fooddelivery.databinding.RestaurantItemBinding
import java.time.format.DateTimeFormatter

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