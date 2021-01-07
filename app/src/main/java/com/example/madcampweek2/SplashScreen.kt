package com.example.madcampweek2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_splash_screen.*
import org.json.JSONException
import org.json.JSONObject


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        button.setOnClickListener {
//            if (true) {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
            VolleyService.testVolley(this) {testSuccess ->
                if (testSuccess) {
                    Toast.makeText(this, "통신 성공!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "통신 실패!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}


