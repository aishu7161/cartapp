package com.example

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CartListModel(
    @SerializedName("product_id"      ) var productId      : Int,
    @SerializedName("product_img"     ) var productImg     : String,
    @SerializedName("product_name"    ) var productName    : String,
    @SerializedName("Discount"        ) var Discount       : Double,
    @SerializedName("color"           ) var color          : String,
    @SerializedName("status_select"   ) var statusSelect   : Boolean,
    @SerializedName("price"           ) var price          : String,
    @SerializedName("count"           ) var count : Int
): Serializable
