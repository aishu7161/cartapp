package com.example

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterMyCartList(
    val context: Context,
    private val myCartList: ArrayList<CartListModel>,
    private val onClick: clickListener
) : RecyclerView.Adapter<AdapterMyCartList.ViewHolder>() {
    inner class ViewHolder(data: View) : RecyclerView.ViewHolder(data) {
        private val productName: TextView? = data.findViewById(R.id.txt_add_cart_product_name)
        private val productColor: TextView? = data.findViewById(R.id.txt_add_cart_product_color)
        val productImage: ImageView? = data.findViewById(R.id.img_add_cart_product)
        val addProductImage: ImageView? = data.findViewById(R.id.img_add_product)
        val removeProductImage: ImageView? = data.findViewById(R.id.img_remove_product)
        val productDiscount: TextView = data.findViewById(R.id.txt_add_cart_product_discount)
        val productPrice: TextView = data.findViewById(R.id.txt_add_cart_product_price)
        val addCount: TextView = data.findViewById(R.id.txt_add_num_of_product)
        private val originalPrice: TextView = data.findViewById(R.id.txt_add_cart_product_original_price)

        @SuppressLint("SetTextI18n")

        fun bindData(modelProduct: CartListModel) {
            productName?.text = modelProduct.productName
            productColor?.text = modelProduct.color
            originalPrice.text = "M.R.P.: Rs:${modelProduct.price}"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterMyCartList.ViewHolder {
        val myContactInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.inflate_add_to_cart_product, parent, false)
        return ViewHolder(myContactInflater)
    }

    override fun onBindViewHolder(holder: AdapterMyCartList.ViewHolder, position: Int) {
        holder.bindData(myCartList[holder.adapterPosition])
        val productModel = myCartList[position]

        holder.productDiscount.text = "${productModel.Discount}%"
        holder.addCount.text = productModel.count.toString()
        var countAdd = holder.addCount.text.toString().toInt()


        val discount = (productModel.Discount / 100) * (productModel.price).toDouble()
        val price = ((productModel.price).toDouble() - (discount)).toInt()
        val amount = price * countAdd
        holder.productPrice?.text = "Rs.${amount}"



        Glide.with(context)
            .load(productModel.productImg)
            .centerCrop()
            .placeholder(R.drawable.phone)
            .into(holder.productImage!!)


        holder.addProductImage?.setOnClickListener {
            countAdd += 1
            onClick.addProduct(countAdd, productModel.productId)
        }

        holder.removeProductImage?.setOnClickListener {
            if (countAdd > 1) {
                countAdd -= 1
                onClick.addProduct(countAdd, productModel.productId)
            }
        }
    }

    override fun getItemCount(): Int {
        return myCartList.size
    }

    interface clickListener {
        fun addProduct(count: Int, id: Int)
    }
}



