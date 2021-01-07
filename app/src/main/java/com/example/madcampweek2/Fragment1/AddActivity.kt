package com.example.madcampweek2.Fragment1

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.madcampweek2.R
import kotlinx.android.synthetic.main.activity_add.*
import java.util.*
import kotlin.collections.ArrayList

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // Request Code
        val ADD_CODE : Int = 0
        val ADD_FAIL : Int = 1

        add_button.setOnClickListener{

            // 입력된 데이터 받기
            val name = add_name.text.toString()
            val number = add_number.text.toString()

            // PhoneBookDataList에 추가
            val bookDataList : ArrayList<PhoneBookData>? = BookDataList.getInstance()
            if (name != "" && number !="" && name != null && number != null) {
                val data: PhoneBookData = PhoneBookData(name, number)
                bookDataList?.add(data)
                Collections.sort(bookDataList)
                setResult(ADD_CODE)
            }else {
                setResult(ADD_FAIL)
            }
            // 액티비티 종료
            finish()
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
}