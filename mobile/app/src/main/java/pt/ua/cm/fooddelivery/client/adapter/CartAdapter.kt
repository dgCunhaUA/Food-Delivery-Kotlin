package pt.ua.cm.fooddelivery.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.client.entities.OrderWithMenus
import pt.ua.cm.fooddelivery.databinding.CartItemBinding
import timber.log.Timber

class CartAdapter(
    private val orderWithMenus: OrderWithMenus,
    private val clickListener: pt.ua.cm.fooddelivery.client.adapter.MenuItemClickListener
): RecyclerView.Adapter<pt.ua.cm.fooddelivery.client.adapter.CartViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pt.ua.cm.fooddelivery.client.adapter.CartViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CartItemBinding.inflate(from, parent, false)
        return pt.ua.cm.fooddelivery.client.adapter.CartViewHolder(parent.context,
            binding,
            clickListener)
    }

    override fun onBindViewHolder(holder: pt.ua.cm.fooddelivery.client.adapter.CartViewHolder, position: Int) {
        Timber.i("BIND: ${orderWithMenus.menus[position]}")
        Timber.i(orderWithMenus.menus[position].toString())
        holder.bindCartItem(orderWithMenus.menus[position])
    }

    override fun getItemCount(): Int = orderWithMenus.menus.size
}