package com.example.madcampweek2.Fragment1

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.madcampweek2.R
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_add.image
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.fragment_1.*
import java.util.*
import kotlin.collections.ArrayList

class AddActivity : AppCompatActivity() {

    private val OPEN_GALLERY = 1
    private var uri : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // Request Code
        val ADD_CODE : Int = 0
        val ADD_FAIL : Int = 1

        Glide.with(image).load(R.drawable.plus).circleCrop().into(image)

        add_button.setOnClickListener{

            // 입력된 데이터 받기
            val name = add_name.text.toString()
            val number = add_number.text.toString()

            // PhoneBookDataList에 추가
            val bookDataList : ArrayList<PhoneBookData>? = BookDataList.getInstance()
            if (name != "" && number !="" && name != null && number != null) {
                val data: PhoneBookData = PhoneBookData(uri, name, number)
                bookDataList?.add(data)
                Collections.sort(bookDataList)
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