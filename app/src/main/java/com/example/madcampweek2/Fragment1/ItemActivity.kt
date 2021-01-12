package com.example.madcampweek2.Fragment1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.madcampweek2.R
import com.example.madcampweek2.RetroFit.RetrofitClient
import kotlinx.android.synthetic.main.activity_item.*
import okhttp3.ResponseBody
import java.util.*


class ItemActivity : AppCompatActivity() {

    var position : Int = 0
    private lateinit var imageData : ByteArray
    private var fab_open: Animation? = null
    private  var fab_close:Animation? = null
    private var isFabOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        // 여러 인텐트를 구분하기 위한 result code
        val DELETE_CODE : Int = 2

        // MainActivity의 intent를 받아 저장.
        val intent = getIntent()
        val id = intent.getStringExtra("id")
        val url = intent.getStringExtra("url")
        val name = intent.getStringExtra("name")
        val number = intent.getStringExtra("number")
        position = intent.getIntExtra("position", 0)

        // hold
        if (url != null) {
            val image = this.findViewById<ImageView>(R.id.image)
            Glide.with(image).load(url).circleCrop().into(image)
        } else {
            Glide.with(image).load(R.drawable.user).circleCrop().into(image)
        }
        text_name.text = name
        text_number.text = number

        call_button.setOnClickListener{
            val uri = Uri.parse("tel:${number.toString()}")
            val intent = Intent(Intent.ACTION_CALL, uri)
            startActivity(intent)
        }

        fab.setOnClickListener{
            anim();
        }

        edit_button.setOnClickListener{

            // 화면전환 (intent 객체 생성), Edit
            val intent = Intent(this, EditActivity::class.java)

            // bundle 객체 생성, contents 저장
            val bundle = Bundle()
            bundle.putString("id", id)
            bundle.putString("url", url)
            bundle.putString("name", name)
            bundle.putString("number", number)
            bundle.putInt("position", position)

            intent.putExtras(bundle)    // intent 객체에 Bundle을 저장

            startActivityForResult(intent, 0)       // 화면전환
        }

        delete_button.setOnClickListener{
            val contactDeleteReq = RetrofitClient.instance.apiService.contactsDelete(id)
            contactDeleteReq?.enqueue(object : retrofit2.Callback<ResponseBody?> {
                override fun onResponse(
                    call: retrofit2.Call<ResponseBody?>?,
                    response: retrofit2.Response<ResponseBody?>
                ) {
                    Toast.makeText(this@ItemActivity, "전송 성공", Toast.LENGTH_LONG).show()
                    val bookDataList: ArrayList<PhoneBookData>? = BookDataList.getInstance()
                    bookDataList?.removeAt(position)
                    setResult(DELETE_CODE)
                    // 액티비티 종료
                    finish()
                }

                override fun onFailure(call: retrofit2.Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(this@ItemActivity, "전송 실패", Toast.LENGTH_LONG).show()
                }
            })
        }
    }



    fun anim() {
        isFabOpen = if (isFabOpen) {
            edit_button.startAnimation(fab_close)
            delete_button.startAnimation(fab_close)
            edit_button.setClickable(false)
            delete_button.setClickable(false)
            false
        } else {
            edit_button.startAnimation(fab_open)
            delete_button.startAnimation(fab_open)
            edit_button.setClickable(true)
            delete_button.setClickable(true)
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == -1) {
            Toast.makeText(this, "이름과 번호를 정확히 입력해주세요!", Toast.LENGTH_LONG).show()
        } else {
            val bookDataList : ArrayList<PhoneBookData>? = BookDataList.getInstance()

            if (bookDataList?.get(resultCode)!!.url != null) {
                val image = this.findViewById<ImageView>(R.id.image)
                Glide.with(image).load(bookDataList?.get(resultCode)!!.url).circleCrop().into(image)
            } else {
                Glide.with(image).load(R.drawable.user).circleCrop().into(image)
            }
            text_name.text = bookDataList?.get(resultCode)!!.name
            text_number.text = bookDataList?.get(resultCode).number
        }
    }
}