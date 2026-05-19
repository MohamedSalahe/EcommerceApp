package com.example.project

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.databinding.CustomerProductBinding
import com.example.project.model.ProductItemDetails

class CustomerProductItems(private val onQuantityChanged: (String, Int) -> Unit, private val onDetailsClicked: (ProductItemDetails) -> Unit) : ListAdapter<ProductItemDetails, CustomerProductItems.ProductViewHolder>(ItemsDiffCallback()) {

    private val quantities = mutableMapOf<String, Int>()

    inner class ProductViewHolder(private val binding: CustomerProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductItemDetails) {
            val productId  = item.productID
            val stock      = item.stock.toIntOrNull() ?: 0
            val currentQty = quantities[productId] ?: 0
            quantities[productId] = currentQty

            binding.productName.text  = item.name
            binding.productPrice.text = "$${item.price}"
            binding.tvQuantity.text   = currentQty.toString()

            if (item.image.isNotEmpty()) {
                Glide.with(binding.productImage.context)
                    .load(item.image)
                    .into(binding.productImage)
            }

            binding.Details.setOnClickListener { onDetailsClicked(item) }

            binding.btnAdd.setOnClickListener {
                val qty = quantities[productId] ?: 0
                if (qty < stock) {
                    val newQty = qty + 1
                    quantities[productId] = newQty
                    binding.tvQuantity.text = newQty.toString()
                    onQuantityChanged(productId, newQty)
                } else {
                    Toast.makeText(binding.root.context, "Maximum stock reached", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnMinus.setOnClickListener {
                val qty = quantities[productId] ?: 0
                if (qty > 0) {
                    val newQty = qty - 1
                    quantities[productId] = newQty
                    binding.tvQuantity.text = newQty.toString()
                    onQuantityChanged(productId, newQty)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = CustomerProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ItemsDiffCallback : DiffUtil.ItemCallback<ProductItemDetails>() {
    override fun areItemsTheSame(oldItem: ProductItemDetails, newItem: ProductItemDetails) =
        oldItem.productID == newItem.productID

    override fun areContentsTheSame(oldItem: ProductItemDetails, newItem: ProductItemDetails) =
        oldItem == newItem
}