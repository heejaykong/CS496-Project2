package com.example.madcampweek2.Fragment1

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.madcampweek2.R
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*
import kotlin.collections.ArrayList

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // get Intent
        val inIntent = getIntent()
        var name = inIntent.getStringExtra("name")
        var number = inIntent.getStringExtra("number")
        val position = inIntent.getIntExtra("position", 0)

        edit_name.setText(name)
        edit_number.setText(number)


        edit_button.setOnClickListener{

            // EDIT REQUEST CODE
            val EDIT_FAIL : Int = -1

            //
            name = edit_name.text.toString()
            number = edit_number.text.toString()

            // PhoneBookDataList에 추가
            val bookDataList : ArrayList<PhoneBookData>? = BookDataList.getInstance()
            if (name != "" && number !="" && name != null && number != null) {
                val data: PhoneBookData = PhoneBookData(name, number)
                bookDataList?.set(position, data)
                Collections.sort(bookDataList)
                bookDataList?.forEachIndexed{ index, phoneBookData ->
                    if (phoneBookData.equals(data)) {
                        setResult(index)
                    }
                }
            }
            else {
                setResult(EDIT_FAIL)
            }

            // 액티비티 종료
            finish();
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