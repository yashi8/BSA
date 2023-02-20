package com.yashishu.bsa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yashishu.bsa.R
import com.yashishu.bsa.models.Product

class ProductAdapter(
    val context: Context,
    val layout: Int,
    val products: List<Product>,

    ) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val textProductTitle = view.findViewById<TextView>(R.id.textProductTitle)
        val imgProductThumb= view.findViewById<ImageView>(R.id.imgProductThumb)


        fun bind(product: Product) {
            textProductTitle.text= product.title
            imgProductThumb.load(product.img_url)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(context).inflate(layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])

    }

    override fun getItemCount()= products.size


}

