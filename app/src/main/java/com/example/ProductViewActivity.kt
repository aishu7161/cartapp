package com.example

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.databinding.ActivityProductViewBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.lang.reflect.Type

class ProductViewActivity : AppCompatActivity(), AdapterChildProduct.UserClick {
    private lateinit var binding: ActivityProductViewBinding

    private var listOfProduct = ArrayList<ModelProduct>()
    private var cartListProduct = ArrayList<CartListModel>()
    private var listOfProductDetails = ArrayList<ModelProduct.ModelForSubProduct>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window: Window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)
        loadJsonFromAssets()

        binding.idViewFlipper.isAutoStart = true
        binding.idViewFlipper.flipInterval = 2500
        binding.idViewFlipper.setInAnimation(applicationContext, android.R.anim.slide_in_left)
        binding.idViewFlipper.setOutAnimation(applicationContext, android.R.anim.slide_out_right)

    }

    private fun loadProduct() {
        val adapter = AdapterParentProduct(listOfProduct, this, this)
        binding.parentRvNotification.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.parentRvNotification.adapter = adapter
    }

    private fun loadJsonFromAssets() {
        try {
            val obj = JSONObject(
                JSONCommon.loadJSONFromAsset(
                    applicationContext,
                    "product_details.json"
                )
            )
            val contentArray = obj.getJSONArray("products")

            for (i in 0 until contentArray.length()) {
                val contentDetail = contentArray.getJSONObject(i)
                val child = contentDetail.getJSONArray("product")
                //
                val gson = Gson()
                val optionType: Type = object :
                    TypeToken<ArrayList<ModelProduct.ModelForSubProduct>?>() {}.type
                val newList: ArrayList<ModelProduct.ModelForSubProduct> =
                    gson.fromJson(child.toString(), optionType)

                listOfProduct.add(
                    ModelProduct(
                        contentDetail.getString("product_title"),
                        newList
                    )
                )
            }

            loadProduct()
            addCount()
            myCartListActivity()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun selectOneProduct(list: ModelProduct.ModelForSubProduct) {
        dialogProduct(list)
    }

    private fun myCartListActivity() {
        binding.imgAddCarts.setOnClickListener {
            val intent = Intent(this, MyCartList::class.java)
            val bundle = Bundle()
            bundle.putSerializable("list", cartListProduct as Serializable)
            intent.putExtra("MyCartList", bundle)
            startActivity(intent)
        }
    }

    @SuppressLint("CutPasteId")
    private fun dialogProduct(list: ModelProduct.ModelForSubProduct) {
        val dialog = Dialog(this)
        // val displayMetrics = DisplayMetrics()
        //  windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.product_detail_dialog)
        val close = dialog.findViewById<ImageView>(R.id.img_close)
        val brandName = dialog.findViewById<TextView>(R.id.txt_brand_name)
        val brandModel = dialog.findViewById<TextView>(R.id.txt_brand_model_name)
        val brandImage = dialog.findViewById<ImageView>(R.id.img_product_name)
        val price = dialog.findViewById<TextView>(R.id.txt_price_discount)
        val discount = dialog.findViewById<TextView>(R.id.txt_discount)
        val color = dialog.findViewById<TextView>(R.id.txt_color_discount)
        val productDetails = dialog.findViewById<TextView>(R.id.txt_product_dec)
        val addFav = dialog.findViewById<ImageView>(R.id.img_fav_add)
        val removeFav = dialog.findViewById<ImageView>(R.id.img_fav)
        val addToCart = dialog.findViewById<TextView>(R.id.txt_add_to_cart)
        val removeToCart = dialog.findViewById<TextView>(R.id.txt_remove_to_cart)

        removeFav.setOnClickListener {
            removeFav.visibility = View.INVISIBLE
            addFav.visibility = View.VISIBLE
        }
        addFav.setOnClickListener {
            removeFav.visibility = View.VISIBLE
            addFav.visibility = View.INVISIBLE
        }

        addToCart.setOnClickListener {
            list.statusSelect = !list.statusSelect
            if (addToCart.text.equals("Remove from Cart")) {
                for (i in 0 until cartListProduct.size) {
                    if (cartListProduct.get(i).productId == list.productId) {
                        cartListProduct.removeAt(i)
                        break
                    }
                }
            } else {
                cartListProduct.add(
                    CartListModel(
                        list.productId,
                        list.productImg,
                        list.productName,
                        list.Discount.toDouble(),
                        list.color,
                        list.statusSelect,
                        list.price,
                        list.count
                    )
                )
            }

            addCount()
            dialog.dismiss()
            Log.e("data", (cartListProduct.size).toString())
        }


        Glide.with(this)
            .load(list.productImg)
            .circleCrop()
            .placeholder(R.drawable.phone)
            .into(brandImage)
        brandName.text = list.brandName
        brandModel.text = list.productName
        price.text = "Rs.${list.price} "

        discount.text = "${list.Discount}%"
        color.text = list.color
        productDetails.text = list.productDetails
        if (!list.statusSelect) {
            addToCart.text = "Add to Cart"
        } else {
            addToCart.text = "Remove from Cart"
            //  listOfProductDetails.remove(list)
        }


        close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog?.window?.let {
            val displayMetrics = DisplayMetrics()
            this?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            val displayWidth = displayMetrics.widthPixels
            val displayHeight = displayMetrics.heightPixels
            //  dialog.getWindow()?.setBackgroundDrawableResource(R.drawable.corner_shape);
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(it.attributes)
            val dialogWindowWidth = (displayWidth * 0.99f).toInt()
            layoutParams.width = dialogWindowWidth
            val dialogWindowHeight = (displayHeight * 0.95f).toInt()
            layoutParams.height = dialogWindowHeight
            it.attributes = layoutParams
        }
    }


    private fun addCount() {
        var count = cartListProduct.size
        binding.txtAddNum.text = "$count"
    }
}


























