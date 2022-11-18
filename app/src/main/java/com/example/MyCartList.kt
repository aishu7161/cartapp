package com.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.databinding.ActivityMyCartListBinding
import com.example.databinding.ActivityProductViewBinding
import com.google.android.material.snackbar.Snackbar

class MyCartList : AppCompatActivity(), AdapterMyCartList.clickListener {

    private lateinit var binding: ActivityMyCartListBinding
    private var listOfParentProduct = ArrayList<ModelProduct>()
    private var listOfChildProductDetails = ArrayList<ModelProduct.ModelForSubProduct>()
    private var cartList = ArrayList<CartListModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCartListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window: Window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)
        getCartProduct()
    }

    private fun getCartProduct() {
        val data = intent.getBundleExtra("MyCartList")
        cartList = data?.getSerializable("list") as ArrayList<CartListModel>

        if (cartList.isEmpty()) {
            binding.emptyCartImage.visibility = View.VISIBLE
            //Toast.makeText(this, "No product", Toast.LENGTH_SHORT).show()
        } else {
            binding.emptyCartImage.visibility = View.INVISIBLE
            recyclerInit()
            productBillDetails()
        }
        binding.backArrowAboutUs.setOnClickListener() {
            onBackPressed()
           // startActivity(Intent(this, ProductViewActivity::class.java))
           // finish()
        }
    }

    private fun recyclerInit() {
        val myCartListAdapter = AdapterMyCartList(this, cartList, this)
        binding.rvMyCartList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvMyCartList.adapter = myCartListAdapter


        myCartListAdapter.notifyDataSetChanged()
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                val deletedProduct: CartListModel =
                    cartList.get(viewHolder.adapterPosition)

                // below line is to get the position
                // of the item at that position.
                val position = viewHolder.adapterPosition

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                cartList.removeAt(viewHolder.adapterPosition)

                // below line is to notify our item is removed from adapter.
                myCartListAdapter.notifyItemRemoved(viewHolder.adapterPosition)


                Snackbar.make(binding.rvMyCartList, "Deleted " + deletedProduct.productName, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo", View.OnClickListener {
                           
                            // below line is to add our item to array list with a position.
                            cartList.add(position,deletedProduct)

                            // below line is to notify item is
                            // added to our adapter class.
                            myCartListAdapter.notifyItemInserted(position)
                        }).show()
            }

        }).attachToRecyclerView(binding.rvMyCartList)

    }

    override fun addProduct(count: Int, id: Int) {
        // val addData = listOfChildProductDetails[position]
        for (i in cartList) {
            if (i.productId == id) {
                i.count = count
                binding.rvMyCartList.adapter?.notifyDataSetChanged()
                productBillDetails()
                break
            }
        }
    }

    private fun productBillDetails() {
        var amount = 0
        var deliveryCharge = 0
        var deliveryChargeText = "Free Delivery"
        var tax = 0

        for (i in cartList) {
            val discount = (i.Discount / 100) * (i.price).toDouble()
            val totalAmount = ((i.price).toDouble() - (discount)).toInt()
            val price = totalAmount * (i.count)

            amount += price
        }
        if (amount <= 500) {

            deliveryCharge = 100

        } else if (amount in 501..1000) {

            deliveryCharge = 50

        } else if (amount in 1001..2000) {

            deliveryCharge = 40

        } else {
            deliveryCharge = 0
         //   binding.txtDeliveryChargeRate.text = "Free Delivery"
        }


        if (amount <= 10000) {

            var tax = 0L
            tax = (5.0 / 100 * amount).toLong()

        } else if (amount in 10001..20000) {

            tax = (6.0 / 100 * amount).toInt()

        } else if (amount in 20001..25000) {

            tax = (7.0 / 100 * amount).toInt()

        } else {

            tax = (9.0 / 100 * amount).toInt()
        }
        binding.txtPriceValue.text = amount.toString()
        binding.txtTaxRate.text = tax.toString()
        binding.txtDeliveryChargeRate.text = deliveryCharge.toString()
        val totalAmount = amount + tax + deliveryCharge
        binding.txtTotalAmountRate.text = "Rs.$totalAmount"

    }


}


