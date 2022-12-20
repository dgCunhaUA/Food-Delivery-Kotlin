package pt.ua.cm.fooddelivery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.databinding.CartItemBinding
import pt.ua.cm.fooddelivery.entities.OrderWithMenus
import timber.log.Timber

class CartAdapter(
    private val orderWithMenus: OrderWithMenus,
    private val clickListener: MenuItemClickListener
): RecyclerView.Adapter<CartViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CartItemBinding.inflate(from, parent, false)
        return CartViewHolder(parent.context, binding, clickListener)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        Timber.i("BIND: ${orderWithMenus.menus[position]}")
        Timber.i(orderWithMenus.menus[position].toString())
        holder.bindCartItem(orderWithMenus.menus[position])
    }

    override fun getItemCount(): Int = orderWithMenus.menus.size
}