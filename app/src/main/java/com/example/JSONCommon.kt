package com.example

import android.content.Context
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class JSONCommon {
    companion object {
         fun loadJSONFromAsset(context: Context,fileName:String): String {
            val json: String?
            try {
                val inputStream = context.assets.open(fileName)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                val charset: Charset = Charsets.UTF_8
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, charset)
                Log.e("QAcommon","all values<---->"+json.toString())
            } catch (ex: IOException) {
                Log.e("QAcommon","Exception<---->"+ex.message)
                return ""
            }
            return json
        }

    }
}