package com.example.madcampweek2.Fragment1

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.madcampweek2.R
import com.example.madcampweek2.SplashScreen.Companion.context
import com.example.madcampweek2.VolleyService
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit.edit_button
import kotlinx.android.synthetic.main.activity_edit.image
import kotlinx.android.synthetic.main.activity_item.*
import java.util.*
import kotlin.collections.ArrayList

class EditActivity : AppCompatActivity() {

    private val OPEN_GALLERY = 1
    private var uri : String? = null
    private var position = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // get Intent
        val inIntent = getIntent()
        var id = inIntent.getStringExtra("id")
        var name = inIntent.getStringExtra("name")
        var number = inIntent.getStringExtra("number")
        uri = inIntent.getStringExtra("photoURI")
        position = inIntent.getIntExtra("position", 0)

        // set initial view
        edit_name.setText(name)
        edit_number.setText(number)
        if (uri != null) {
            val image = this.findViewById<ImageView>(R.id.image)
            Glide.with(image).load(uri).circleCrop().into(image)
        } else {
            Glide.with(image).load(R.drawable.plus).circleCrop().into(image)
        }

        // image click event
        image.setOnClickListener {
            val intent : Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, OPEN_GALLERY)
        }

        // edit button click event
        edit_button.setOnClickListener{

            // EDIT REQUEST CODE
            val EDIT_FAIL : Int = -1

            //
            name = edit_name.text.toString()
            number = edit_number.text.toString()

            // PhoneBookDataList에 추가
            val bookDataList : ArrayList<PhoneBookData>? = BookDataList.getInstance()
            if (name != "" && number !="" && name != null && number != null) {
                val data: PhoneBookData = PhoneBookData(id, uri, name, number)
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

    override fun onBackPressed() {
        setResult(position)
        finish()
    }
}