package com.yashishu.bsa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yashishu.bsa.databinding.ProductCard1Binding
import com.yashishu.bsa.models.Product

class ProductSliderAdapter(
    private val listener: (Product) -> Unit
) : ListAdapter<Product, ProductSliderAdapter.ViewHolder>(ProductDiffUtil()) {
    class ViewHolder(
        private val binding: ProductCard1Binding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductCard1Binding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { listener(getItem(position)) }
    }

}