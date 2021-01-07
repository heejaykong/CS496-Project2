package com.example.madcampweek2.Fragment3

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.madcampweek2.R
import kotlinx.android.synthetic.main.activity_game2.*

class GameActivity2 : AppCompatActivity() {

    lateinit var animalList : ArrayList<String>
    lateinit var jobList : ArrayList<String>
    lateinit var movieList : ArrayList<String>
    lateinit var realuseList : ArrayList<String>
    lateinit var worldBestList : ArrayList<String>
    lateinit var greatList : ArrayList<String>
    lateinit var foodList : ArrayList<String>
    lateinit var sportsList : ArrayList<String>
    lateinit var celebList : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2)

        // Set answer list
        animalList = arrayListOf("사자", "호랑이", "판다", "나무늘보", "코뿔소", "고슴도치", "곰", "하마")
        jobList = arrayListOf("개발자", "소방관", "바리스타", "PD")
        movieList = arrayListOf("라라랜드", "인셉션", "어벤져스", "놈놈놈")
        realuseList = arrayListOf("와이파이", "단톡방", "핸드폰", "텀블러")
        worldBestList = arrayListOf("God창수")
        greatList = arrayListOf("이순신", "세종대왕", "장영실")
        foodList = arrayListOf("짜장면", "초밥", "순댓국", "냉면")
        sportsList = arrayListOf("탁구", "클라이밍", "야구", "스키점프")
        celebList = arrayListOf("용이형")

        game1.setOnClickListener{
            val number_temp = edit_number.text.toString()
            val lier_temp = edit_lier.text.toString()
            gotolier(number_temp, lier_temp, animalList)
        }
        game2.setOnClickListener{
            val number_temp = edit_number.text.toString()
            val lier_temp = edit_lier.text.toString()
            gotolier(number_temp, lier_temp, jobList)
        }
        game3.setOnClickListener{
            val number_temp = edit_number.text.toString()
            val lier_temp = edit_lier.text.toString()
            gotolier(number_temp, lier_temp, movieList)
        }
        game4.setOnClickListener{
            val number_temp = edit_number.text.toString()
            val lier_temp = edit_lier.text.toString()
            gotolier(number_temp, lier_temp, realuseList)
        }
        game5.setOnClickListener{
            val number_temp = edit_number.text.toString()
            val lier_temp = edit_lier.text.toString()
            gotolier(number_temp, lier_temp, worldBestList)
        }
        game6.setOnClickListener{
            val number_temp = edit_number.text.toString()
            val lier_temp = edit_lier.text.toString()
            gotolier(number_temp, lier_temp, greatList)
        }
        game7.setOnClickListener{
            val number_temp = edit_number.text.toString()
            val lier_temp = edit_lier.text.toString()
            gotolier(number_temp, lier_temp, foodList)
        }
        game8.setOnClickListener{
            val number_temp = edit_number.text.toString()
            val lier_temp = edit_lier.text.toString()
            gotolier(number_temp, lier_temp, sportsList)
        }
        game9.setOnClickListener{
            val number_temp = edit_number.text.toString()
            val lier_temp = edit_lier.text.toString()
            gotolier(number_temp, lier_temp, celebList)
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

    fun gotolier(number_ : String, lier_ : String, list : ArrayList<String>) {
        if (number_ != "" && lier_ !="" && number_ != null && lier_ != null) {

            val number = number_.toInt()
            val lier = lier_.toInt()

            if (lier < 1 || lier > number) {
                Toast.makeText(this, "인원 수를 정확히 입력해주세요!", Toast.LENGTH_LONG).show()
            }
            else {
                val num = java.util.Random().nextInt(list.size)
                val answer : String = list.get(num)

                // 라이어 activity로 넘기기 위한 intent 정의
                val intent = Intent(this, LierActivity::class.java)

                // bundle 객체 생성, contents 저장
                val bundle = Bundle()
                bundle.putString("answer", answer)
                bundle.putInt("number", number)
                bundle.putInt("lier", lier)
                intent.putExtras(bundle)    // intent 객체에 Bundle을 저장

                startActivityForResult(intent, 0)
            }
        } else {
            Toast.makeText(this, "인원 수를 정확히 입력해주세요!", Toast.LENGTH_LONG).show()
        }
    }
}