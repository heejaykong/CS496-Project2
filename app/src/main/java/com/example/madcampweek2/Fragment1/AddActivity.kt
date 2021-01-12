package com.example.madcampweek2.Fragment1

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.madcampweek2.MainActivity
import com.example.madcampweek2.R
import com.example.madcampweek2.RetroFit.ApiService
import com.example.madcampweek2.RetroFit.RetrofitClient
import kotlinx.android.synthetic.main.activity_add.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddActivity : AppCompatActivity() {

    lateinit var apiService : ApiService
    private val OPEN_GALLERY = 1
    private var uri : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // Request Code
        val ADD_CODE : Int = 0
        val ADD_FAIL : Int = 1
        apiService = RetrofitClient.instance.apiService

        Glide.with(image).load(R.drawable.plus).circleCrop().into(image)

        add_button.setOnClickListener{

            // 입력된 데이터 받기
            val name = add_name.text.toString()
            val number = add_number.text.toString()

            // PhoneBookDataList에 추가
            val bookDataList : ArrayList<PhoneBookData>? = BookDataList.getInstance()
            if (name != "" && number !="" && name != null && number != null) {

                // 서버에 연락처 정보 전송
                var body : MultipartBody.Part? = null
                val imageData = RetrofitClient.instance.uriToByteArrayBody(this, uri?.toUri())
                if (imageData != null) {
                    val reqfile: RequestBody = RequestBody.create(MediaType.parse("image/*"), imageData)
                    body = MultipartBody.Part.createFormData("upload", "abc", reqfile)
                }
                val contactPostReq = RetrofitClient.instance.apiService.contactsPost(name, number, body)
                contactPostReq?.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>?,
                        response: Response<ResponseBody?>
                    ) {
                        val test = response.body()!!.string()
                        val test_ = JSONObject(test).getString("_id")
                        val url = JSONObject(test).getString("url")
                        val item : PhoneBookData = PhoneBookData(test_, url, name, number)
                        bookDataList?.add(item)
                        Collections.sort(bookDataList)
                        Toast.makeText(this@AddActivity, "전송 성공", Toast.LENGTH_LONG).show()
                    }
                    override fun onFailure(call: retrofit2.Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(this@AddActivity, "전송 실패", Toast.LENGTH_LONG).show()
                    }
                })
                setResult(ADD_CODE)
            }else {
                setResult(ADD_FAIL)
            }
            // 액티비티 종료
            finish()
        }

        image.setOnClickListener {
            val intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, OPEN_GALLERY)
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_GALLERY) {
                uri = data?.data.toString()
                if (uri != null) {
                    val image = this.findViewById<ImageView>(R.id.image)
                    Glide.with(image).load(uri).circleCrop().into(image)
                } else {
                    Glide.with(image).load(R.drawable.plus).circleCrop().into(image)
                }
            }
        }
    }
}