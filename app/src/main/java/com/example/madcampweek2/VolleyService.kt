package com.example.madcampweek2

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.*
import com.example.madcampweek2.Fragment1.BookDataList
import com.example.madcampweek2.Fragment1.PhoneBookData
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Method
import java.net.URI


object VolleyService {

    private val ContactUrl = "http://192.249.18.208:3000/contacts"
    private val GalleryUrl = "http://192.249.18.208:3000/gallery"

    fun postContactVolley(context: Context, item: PhoneBookData) {
        val uri = "$ContactUrl/post"

        val requestQueue = Volley.newRequestQueue(context)

        val jsonObject = JSONObject();
        jsonObject.put("name", item.name)
        jsonObject.put("phoneNum", item.number)
        // 2. Request Obejct인 StringRequest 생성
        val request: JsonObjectRequest = JsonObjectRequest(Request.Method.POST, uri, jsonObject,
                Response.Listener {response ->
                    item.id = response.toString()
                }, Response.ErrorListener {})
        requestQueue.add(request)
    }

    fun getContactVolley(context: Context) {
        val uri = "$ContactUrl/getAll"

        val requestQueue = Volley.newRequestQueue(context)

        val jsonArray = JSONArray();
        val request: JsonArrayRequest = JsonArrayRequest(Request.Method.GET, uri, jsonArray, Response.Listener { response ->
            getContactsList(response, context)
        }, Response.ErrorListener { error ->
            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
        }
        )
        requestQueue.add(request)
    }

    fun putContactVolley(context: Context, item: PhoneBookData) {
        val uri = "$ContactUrl/contacts/${item.id}"

        val requestQueue = Volley.newRequestQueue(context)

        val jsonObject = JSONObject();
        jsonObject.put("name", item.name)
        jsonObject.put("phoneNum", item.number)
        // 2. Request Obejct인 StringRequest 생성
        val request: JsonObjectRequest = JsonObjectRequest(Request.Method.POST, uri, jsonObject,
                Response.Listener {}, Response.ErrorListener {})

        requestQueue.add(request)
    }

    fun deleteContactVolley(context: Context, id : String) {
        val url = "$ContactUrl/delete"

        val requestQueue = Volley.newRequestQueue(context)

        val jsonObject = JSONObject();
        jsonObject.put("_id", id)
        // 2. Request Obejct인 StringRequest 생성
        val request: JsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                Response.Listener {}, Response.ErrorListener {})

        requestQueue.add(request)
    }

    fun getContactsList(response: JSONArray, context: Context) {
        try {
            response.let {
                for (i in 0 until response.length()) {
                    val jresponse : JSONObject = response.getJSONObject(i)
                    val item = PhoneBookData(jresponse.getString("_id"), null, jresponse.getString("name"), jresponse.getString("phoneNum"))
                    val bookDataList = BookDataList.getInstance()
                    bookDataList!!.add(item)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun sendImage(context: Context, byteArray: String) {
        val url = "http://192.249.18.208:3000/images/post"

        if (byteArray != null) {
            val requestQueue = Volley.newRequestQueue(context)

//             2. Request Obejct인 StringRequest 생성
            val request: StringRequest = object : StringRequest(Method.POST, url,
                    Response.Listener { response ->

                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
                    }
            ) {
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["image"] = byteArray
                    return params
                }
            }
//            request.headers["Content-Type"] = "multipart/form-data"
            requestQueue.add(request)
        }
    }



    fun getImage(context: Context, view: NetworkImageView, url: String) {

        val requestQueue = Volley.newRequestQueue(context)
        val request = ImageRequest(url,
                Response.Listener<Bitmap> {
                    fun onResponse(bitmap: Bitmap) {
                        view.setImageBitmap(bitmap)
                    }
                }, 0, 0, null,
                Response.ErrorListener {
                    fun onErrorResponse(error: VolleyError) {
                        view.setImageResource(R.drawable.loading)
                    }
                })

        requestQueue.add(request)
    }

    fun uriToByteArray (context: Context, uri : Uri) : String? {
        // uri to bit array
        try {
            val inputStream : InputStream? = context.contentResolver.openInputStream(uri);
            var buffer : ByteArray = ByteArray(1024)
            val byteArrayOutputStream = ByteArrayOutputStream(buffer.size);
            var len = 0
            while (inputStream!!.read(buffer).apply{len = this} != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            val imageData = byteArrayOutputStream.toByteArray()
            val sendingItem = String(imageData)

            return sendingItem

        } catch (e : FileNotFoundException) {
            e.printStackTrace();
        } catch (e : IOException) {
            e.printStackTrace();
        }
        return null
    }


    fun ByteArray.encodeBase64(): ByteArray {
        val table = (CharRange('A', 'Z') + CharRange('a', 'z') + CharRange('0', '9') + '+' + '/').toCharArray()
        val output = ByteArrayOutputStream()
        var padding = 0
        var position = 0
        while (position < this.size) {
            var b = this[position].toInt() and 0xFF shl 16 and 0xFFFFFF
            if (position + 1 < this.size) b = b or (this[position + 1].toInt() and 0xFF shl 8) else padding++
            if (position + 2 < this.size) b = b or (this[position + 2].toInt() and 0xFF) else padding++
            for (i in 0 until 4 - padding) {
                val c = b and 0xFC0000 shr 18
                output.write(table[c].toInt())
                b = b shl 6
            }
            position += 3
        }
        for (i in 0 until padding) {
            output.write('='.toInt())
        }
        return output.toByteArray()
    }

    fun ByteArray.decodeBase64(): ByteArray {
        val table = intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1,
                -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
                -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1)

        val output = ByteArrayOutputStream()
        var position = 0
        while (position < this.size) {
            var b: Int
            if (table[this[position].toInt()] != -1) {
                b = table[this[position].toInt()] and 0xFF shl 18
            } else {
                position++
                continue
            }
            var count = 0
            if (position + 1 < this.size && table[this[position + 1].toInt()] != -1) {
                b = b or (table[this[position + 1].toInt()] and 0xFF shl 12)
                count++
            }
            if (position + 2 < this.size && table[this[position + 2].toInt()] != -1) {
                b = b or (table[this[position + 2].toInt()] and 0xFF shl 6)
                count++
            }
            if (position + 3 < this.size && table[this[position + 3].toInt()] != -1) {
                b = b or (table[this[position + 3].toInt()] and 0xFF)
                count++
            }
            while (count > 0) {
                val c = b and 0xFF0000 shr 16
                output.write(c.toChar().toInt())
                b = b shl 8
                count--
            }
            position += 4
        }
        return output.toByteArray()
    }
}