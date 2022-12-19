package pt.ua.cm.fooddelivery.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.databinding.MenuItemBinding
import pt.ua.cm.fooddelivery.restaurant.RestaurantWithMenus

class MenuAdapter(
    private val restaurantWithMenus: RestaurantWithMenus
): RecyclerView.Adapter<MenuViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = MenuItemBinding.inflate(from, parent, false)
        return MenuViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bindMenuItem(restaurantWithMenus.menus[position])
    }

    override fun getItemCount(): Int = restaurantWithMenus.menus.size
}