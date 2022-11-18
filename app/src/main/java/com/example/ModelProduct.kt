package com.example

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ModelProduct(
    @SerializedName("product_title") var productTitle : String,
    @SerializedName("product") var product : ArrayList<ModelForSubProduct> = arrayListOf()

): Serializable
{
    data class ModelForSubProduct(
        @SerializedName("product_id"      ) var productId      : Int,
        @SerializedName("product_img"     ) var productImg     : String,
        @SerializedName("product_name"    ) var productName    : String,
        @SerializedName("brand_name"      ) var brandName      : String,
        @SerializedName("product_details" ) var productDetails : String,
        @SerializedName("Discount"        ) var Discount       : Int,
        @SerializedName("color"           ) var color          : String,
        @SerializedName("status_select"   ) var statusSelect   : Boolean,
        @SerializedName("price"           ) var price          : String,
        @SerializedName("count"           )  var count : Int
    ):Serializable
}
