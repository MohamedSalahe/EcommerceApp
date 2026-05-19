package com.example.project

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.databinding.CartItemsBinding
import com.example.project.model.CartItem

class CartItemsAdapter(
    private val onQuantityChanged: (String, Int) -> Unit
) : ListAdapter<CartItem, CartItemsAdapter.CartViewHolder>(CartDiffCallback()) {

    private val quantities = mutableMapOf<String, Int>()

    inner class CartViewHolder(private val binding: CartItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            val currentQty = quantities[item.productId] ?: item.cartQuantity
            quantities[item.productId] = currentQty

            binding.productName.text  = item.name
            binding.tvQuantity.text   = currentQty.toString()
            binding.priceView.text = "Total: $${"%.2f".format(item.price * currentQty)}"

            if (item.imageUrl.isNotEmpty()) {
                Glide.with(binding.productImage.context)
                    .load(item.imageUrl)
                    .into(binding.productImage)
            }

            binding.btnAdd.setOnClickListener {
                val qty = quantities[item.productId] ?: 0
                if (qty < item.stock) {
                    val newQty = qty + 1
                    quantities[item.productId] = newQty
                    binding.tvQuantity.text   = newQty.toString()
                    binding.priceView.text = "Total: $${"%.2f".format(item.price * newQty)}"
                    onQuantityChanged(item.productId, newQty)
                } else {
                    Toast.makeText(binding.root.context, "Maximum stock reached", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnMinus.setOnClickListener {
                val qty = quantities[item.productId] ?: 0
                if (qty > 0) {
                    val newQty = qty - 1
                    quantities[item.productId] = newQty
                    binding.tvQuantity.text   = newQty.toString()
                    binding.priceView.text = "Total: $${"%.2f".format(item.price * newQty)}"
                    onQuantityChanged(item.productId, newQty)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
        oldItem.productId == newItem.productId
    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
        oldItem == newItem
}
