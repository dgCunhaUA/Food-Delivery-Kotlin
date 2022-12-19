package pt.ua.cm.fooddelivery.cart

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.databinding.CartItemBinding
import pt.ua.cm.fooddelivery.menu.Menu


class CartViewHolder(
    private val context: Context,
    private val binding: CartItemBinding,
): RecyclerView.ViewHolder(binding.root)
{

    fun bindCartItem(menu: Menu)
    {
        binding.cartMenuId.text = menu.menuId.toString()
    }
}