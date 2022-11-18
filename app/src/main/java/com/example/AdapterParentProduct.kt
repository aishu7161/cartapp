package com.example

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AdapterParentProduct(
    private val productArray: ArrayList<ModelProduct>,
    private val myContext: Context,
    private val childAdapterProduct: AdapterChildProduct.UserClick
) : RecyclerView.Adapter<AdapterParentProduct.ViewHolder>() {

    inner class ViewHolder(data: View) : RecyclerView.ViewHolder(data) {
        val txtProductTitle: TextView = data.findViewById(R.id.txt_product_title)
        val childRecycler: RecyclerView = data.findViewById(R.id.child_rv_Product)
        val productCount: TextView = data.findViewById(R.id.txtSelectAllParent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val myInflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.inflate_product_parent, parent, false)
        return ViewHolder(myInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = productArray[position]
        holder.txtProductTitle.text = data.productTitle
        holder.productCount.text = data.product.size.toString()
        setChildRecyclerAdapter(myContext, holder.childRecycler, position)
    }


    private fun setChildRecyclerAdapter(
        context: Context,
        recycler: RecyclerView,
        position: Int
    ) {
        val adapter =
            AdapterChildProduct(context, childAdapterProduct, productArray[position].product)
        recycler.layoutManager =
            LinearLayoutManager(myContext, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter
        recycler.addItemDecoration(ItemDecorationIndicator())
    }


    override fun getItemCount(): Int {
        return productArray.size
    }
}

