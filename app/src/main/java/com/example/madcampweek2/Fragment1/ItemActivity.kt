package com.example.madcampweek2.Fragment1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.madcampweek2.R
import kotlinx.android.synthetic.main.activity_item.*
import java.util.ArrayList


class ItemActivity : AppCompatActivity() {

    var position : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        // 여러 인텐트를 구분하기 위한 result code
        val DELETE_CODE : Int = 2

        // AddActivity의 intent를 받아 저장.
        val intent = getIntent()
        val name = intent.getStringExtra("name")
        val number = intent.getStringExtra("number")
        position = intent.getIntExtra("position", 0)

        text_name.text = name
        text_number.text = number

        call_button.setOnClickListener{
            var phoneNumber = "tel:"
            phoneNumber += number?.get(0)
            phoneNumber += number?.get(1)
            phoneNumber += number?.get(2)
            phoneNumber += "-"
            phoneNumber += number?.get(3)
            phoneNumber += number?.get(4)
            phoneNumber += number?.get(5)
            phoneNumber += number?.get(6)
            phoneNumber += "-"
            phoneNumber += number?.get(7)
            phoneNumber += number?.get(8)
            phoneNumber += number?.get(9)
            phoneNumber += number?.get(10)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(phoneNumber))
            startActivity(intent)
        }


        edit_button.setOnClickListener{

            // 화면전환 (intent 객체 생성), Edit
            val intent = Intent(this, EditActivity::class.java)

            // bundle 객체 생성, contents 저장
            val bundle = Bundle()
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
            finish();
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == -1) {
            Toast.makeText(this, "이름과 번호를 정확히 입력해주세요!", Toast.LENGTH_LONG).show()
        } else {
            val bookDataList : ArrayList<PhoneBookData>? = BookDataList.getInstance()
            text_name.text = bookDataList?.get(resultCode)!!.name
            text_number.text = bookDataList?.get(resultCode).number
        }
    }
}