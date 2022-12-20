package pt.ua.cm.fooddelivery.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.databinding.CartItemBinding
import pt.ua.cm.fooddelivery.entities.Menu
import timber.log.Timber


class CartViewHolder(
    private val context: Context,
    private val binding: CartItemBinding,
    private val clickListener: MenuItemClickListener
): RecyclerView.ViewHolder(binding.root)
{

    fun bindCartItem(menu: Menu)
    {
        binding.cartMenuId.text = menu.name

        binding.addBtn.setOnClickListener {
            Timber.i("Adding $menu")
            clickListener.addMenuToCart(menu)
        }

        binding.rmBtn.setOnClickListener {
            Timber.i("Removing $menu")
            clickListener.rmMenuFromCart(menu)
        }
    }
}