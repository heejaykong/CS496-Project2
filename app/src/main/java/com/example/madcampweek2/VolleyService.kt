package com.example.madcampweek2

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_splash_screen.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

object VolleyService {
    val testUrl = "http://192.249.18.208:3000/"

    fun testVolley(context: Context, success: (Boolean) -> Unit) {
        val myJson = JSONObject()
        val requestBody = myJson.toString()

        val testRequest = object : StringRequest(Method.POST, testUrl, Response.Listener {response ->
            success(true)
        }, Response.ErrorListener { error ->
            success(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(testRequest)
    }
}