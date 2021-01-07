package com.example.madcampweek2.Fragment3

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.madcampweek2.R
import kotlinx.android.synthetic.main.activity_game4.*


class GameActivity4 : AppCompatActivity() {

    lateinit var rouletteView: RouletteView
    private val itemList: ArrayList<String> = arrayListOf()
    private val initAngle = 0.0f
    var mainhandler: Handler? = null

    private val update:Runnable = object:Runnable{
        @RequiresApi(Build.VERSION_CODES.N)
        override fun run() {
            rouletteView.updateStartAngle()
            if (rouletteView.velocity > 0f){
                mainhandler!!.postDelayed(this, 10)
            }
            else {
                itemList.forEachIndexed { index, item ->

                    if ((rouletteView.startAngle+rouletteView.angle*index < 270
                                && 270 < rouletteView.startAngle+rouletteView.angle*(index+1))) {
                        val answer : String = "$item 당첨!"
                        answer_text.text = answer
                    }
                    else if ((rouletteView.startAngle+rouletteView.angle*index < 630
                                && 630 < rouletteView.startAngle+rouletteView.angle*(index+1))) {
                        val answer : String = "$item 당첨!"
                        answer_text.text = answer
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game4)
        rouletteView = roulette as RouletteView

        start.setOnClickListener {
            if (itemList.isNotEmpty()) {
                rouletteView.velocity += 50f
                mainhandler!!.postDelayed(update, 10)
            }
        }

        add.setOnClickListener {
            val text = edit_text.text.toString()
            if (text != "" && text != null) {
                itemList.add(text)
                rouletteView.manageCircle(itemList)
            } else {
                Toast.makeText(this, "정확히 입력해주세요", Toast.LENGTH_LONG).show()
            }
        }

        delete.setOnClickListener {
            if (itemList.isNotEmpty()) {
                itemList.removeAt(itemList.size-1)
                rouletteView.manageCircle(itemList)
            }
        }

        reset.setOnClickListener {
            if (itemList.isNotEmpty()) {
                itemList.removeAll(itemList)
                rouletteView.manageCircle(itemList)

            }
        }

        mainhandler = Handler(Looper.getMainLooper())
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