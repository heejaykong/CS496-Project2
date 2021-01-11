package com.example.madcampweek2.Fragment1

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.madcampweek2.R
import com.example.madcampweek2.RetroFit.RetrofitClient
import com.example.madcampweek2.VolleyService
import kotlinx.android.synthetic.main.activity_item.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList


class ItemActivity : AppCompatActivity() {

    var position : Int = 0
    private lateinit var imageData : ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

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

        button2.setOnClickListener{
//            val Uri = uri?.toUri()
//            RetrofitClient.instance.uriToByteArray(this, Uri!!)
//            try {
//                RetrofitClient.instance.postContactProfile(imageData)
//            } catch (e : IOException) {
//                e.printStackTrace()
//            }

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
            val bookDataList : ArrayList<PhoneBookData>? = BookDataList.getInstance()
            bookDataList?.removeAt(position)
            setResult(DELETE_CODE)
            // 액티비티 종료
            finish()
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