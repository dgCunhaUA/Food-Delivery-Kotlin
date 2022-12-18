package pt.ua.cm.fooddelivery.pages.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.data.Restaurant
import pt.ua.cm.fooddelivery.databinding.RestaurantItemBinding

class RestaurantAdapter(private val onRestaurantClicked: (Restaurant) -> Unit) :
    ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(DiffCallback) {

    private lateinit var context: Context

    class RestaurantViewHolder(private var binding: RestaurantItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: Restaurant, context: Context) {
            binding.restaurantName.text = context.getString(restaurant.nameResourceId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        context = parent.context
        return RestaurantViewHolder(
            RestaurantItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onRestaurantClicked(current)
            Log.i("RestaurantAdapter", "OnClick Listener $current")
        }
        holder.bind(current, context)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Restaurant>() {
            override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                return (oldItem.nameResourceId == newItem.nameResourceId )
            }

            override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                return oldItem == newItem
            }
        }
    }
}