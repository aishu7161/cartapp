package com.example

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterChildProduct(
    private val context: Context, private val click: UserClick,
    private val subProductArray: ArrayList<ModelProduct.ModelForSubProduct>
) : RecyclerView.Adapter<AdapterChildProduct.ViewHolder>() {
    inner class ViewHolder(data: View) : RecyclerView.ViewHolder(data) {

        val imgProduct: ImageView = data.findViewById(R.id.imgChildProduct)
        private val productName: TextView = data.findViewById(R.id.txt_product_name)

        private val productDiscount: TextView = data.findViewById(R.id.txt_discount)
        fun bindItem(productModel: ModelProduct.ModelForSubProduct) {
            productName.text = productModel.brandName
            productDiscount.text="${productModel.Discount}%"
            if (imgProduct != null) {
                Glide.with(context)
                    .load(productModel.productImg)
                    .circleCrop()
                    .placeholder(R.drawable.phone)
                    .into(imgProduct)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val myInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_child_inflate, parent, false)
        return ViewHolder(myInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItem(subProductArray[holder.adapterPosition])
        val list = subProductArray[position]
        holder.imgProduct.setOnClickListener() {
            click.selectOneProduct(list)

        }
    }

    override fun getItemCount(): Int {
        return subProductArray.size
    }

    interface UserClick {
        fun selectOneProduct(list: ModelProduct.ModelForSubProduct)
    }
}