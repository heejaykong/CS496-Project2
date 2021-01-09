package com.example.madcampweek2

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.madcampweek2.Fragment1.BookDataList
import com.example.madcampweek2.Fragment1.Fragment1
import com.example.madcampweek2.Fragment1.PhoneBookData
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.activity_splash_screen.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

object VolleyService {

    private val ContactUri = "http://192.249.18.208:3000/contacts"

    fun postContactVolley(context: Context, item : PhoneBookData) {
        val uri = "$ContactUri/post"

        val requestQueue = Volley.newRequestQueue(context)

        val jsonObject = JSONObject();
        jsonObject.put("name", item.name)
        jsonObject.put("phoneNum", item.number)
        // 2. Request Obejct인 StringRequest 생성
        val request: JsonObjectRequest = JsonObjectRequest(Request.Method.POST, uri, jsonObject,
                Response.Listener {}, Response.ErrorListener {})
        requestQueue.add(request)
    }

    fun getContactVolley(context: Context) {
        val uri = "$ContactUri/getAll"

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

    fun getContactsList(response: JSONArray, context: Context) {
        try {
            response.let {
                for (i in 0 until response.length()) {
                    val jresponse : JSONObject = response.getJSONObject(i)
                    val item = PhoneBookData(null, jresponse.getString("name"), jresponse.getString("phoneNum"))
                    val bookDataList = BookDataList.getInstance()
                    bookDataList!!.add(item)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}