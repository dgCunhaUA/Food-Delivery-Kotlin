package pt.ua.cm.fooddelivery.menu

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import pt.ua.cm.fooddelivery.databinding.MenuItemBinding
import timber.log.Timber

class MenuViewHolder(
    private val context: Context,
    private val binding: MenuItemBinding,
    private val clickListener: MenuItemClickListener
): RecyclerView.ViewHolder(binding.root)
{

    fun bindMenuItem(menu: Menu)
    {
        binding.menuName.text = menu.name

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