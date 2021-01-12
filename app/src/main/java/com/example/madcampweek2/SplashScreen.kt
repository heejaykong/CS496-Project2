package com.example.madcampweek2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.madcampweek2.RetroFit.RetrofitClient
import com.facebook.*
import com.facebook.GraphRequest.newMeRequest
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_game4.*
import kotlinx.android.synthetic.main.activity_splash_screen.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SplashScreen : AppCompatActivity() {

    val callbackManager = CallbackManager.Factory.create()
    private val REQUEST_PERMISSIONS = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        val accessToken = AccessToken.getCurrentAccessToken()

        val accessTokenTracker : AccessTokenTracker = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(
                oldAccessToken: AccessToken,
                currentAccessToken: AccessToken
            ) {
                if (currentAccessToken == null) {
                    //write your code here what to do when user logout
                    continue_button.visibility = View.INVISIBLE
                }
            }
        }

        if (accessToken != null) {
//            VolleyService.getImage(this, profile, Profile.getCurrentProfile().getProfilePictureUri(50,50).toString())
//            val requestQueue = Volley.newRequestQueue(context)
//            val request = GraphRequest(accessToken,  Profile.getCurrentProfile().getProfilePictureUri(50,50).toString())
            val request = newMeRequest(
                accessToken,
                object : GraphRequest.GraphJSONObjectCallback {
                    override fun onCompleted(
                        `object`: JSONObject?,
                        response: GraphResponse?
                    ) {
                        // Application code
                        if (`object` != null) {
                            profile.profileId = `object`.optString("id")
                        }
                    }
                }).executeAsync()

            profile.visibility = View.VISIBLE

            continue_text.text = "${Profile.getCurrentProfile().firstName}님으로 계속"
            continue_button.visibility = View.VISIBLE
            continue_button.setOnClickListener {
                // App code
                if (accessToken != null) {
                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                    finish()
                }
            }

        } else {
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult?> {


                    override fun onSuccess(loginResult: LoginResult?) {
                        // App code
                        startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                        finish()
                    }
                    override fun onCancel() {}
                    override fun onError(exception: FacebookException?) {
                        // App code
                        Toast.makeText(
                            this@SplashScreen,
                            "Error! Please try aga in!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })

        }

        get_button.setOnClickListener {

            val contactGetReq = RetrofitClient.instance.apiService.contactsGet(id.text.toString())
            contactGetReq?.enqueue(object : retrofit2.Callback<ResponseBody?> {
                override fun onResponse(
                    call: retrofit2.Call<ResponseBody?>?,
                    response: retrofit2.Response<ResponseBody?>
                ) {
                    val test = response.body()!!.string()
                    val test_ = JSONObject(test).getString("phoneNum")
                    number.setText(test_)
                    Toast.makeText(this@SplashScreen, "전송 성공", Toast.LENGTH_LONG).show()

                }
                override fun onFailure(call: retrofit2.Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(this@SplashScreen, "전송 실패", Toast.LENGTH_LONG).show()
                }
            })

        }

        post_button.setOnClickListener {
            val contactPostReq = RetrofitClient.instance.apiService.contactsPost(
                name.text.toString(),
                number.text.toString(),
                null
            )
            contactPostReq?.enqueue(object : retrofit2.Callback<ResponseBody?> {
                override fun onResponse(
                    call: retrofit2.Call<ResponseBody?>?,
                    response: retrofit2.Response<ResponseBody?>
                ) {
                    val test = response.body()!!.string()
                    val test_ = JSONObject(test).getString("_id")


                    id.setText(test_)
                    Toast.makeText(this@SplashScreen, "전송 성공", Toast.LENGTH_LONG).show()
                }
                override fun onFailure(call: retrofit2.Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(this@SplashScreen, "전송 실패", Toast.LENGTH_LONG).show()
                }
            })

        }

        delete_button.setOnClickListener {
            val contactGetReq = RetrofitClient.instance.apiService.contactsDelete(id.text.toString())
            contactGetReq?.enqueue(object : retrofit2.Callback<ResponseBody?> {
                override fun onResponse(
                    call: retrofit2.Call<ResponseBody?>?,
                    response: retrofit2.Response<ResponseBody?>
                ) {
                    Toast.makeText(this@SplashScreen, "전송 성공", Toast.LENGTH_LONG).show()

                }
                override fun onFailure(call: retrofit2.Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(this@SplashScreen, "전송 실패", Toast.LENGTH_LONG).show()
                }
            })
        }

        put_button.setOnClickListener {

            val contactPutReq = RetrofitClient.instance.apiService.contactsPut(
                id.text.toString(),
                name.text.toString(),
                number.text.toString(),
                id.text.toString(),
                null
            )
            contactPutReq?.enqueue(object : retrofit2.Callback<ResponseBody?> {
                override fun onResponse(
                    call: retrofit2.Call<ResponseBody?>?,
                    response: retrofit2.Response<ResponseBody?>
                ) {
                    Toast.makeText(this@SplashScreen, "전송 성공", Toast.LENGTH_LONG).show()
                }
                override fun onFailure(call: retrofit2.Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(this@SplashScreen, "전송 실패", Toast.LENGTH_LONG).show()
                }
            })

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView: View? = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
