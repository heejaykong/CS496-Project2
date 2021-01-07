package com.example.madcampweek2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.madcampweek2.Fragment1.Fragment1
import com.example.madcampweek2.Fragment2.Fragment2
import com.example.madcampweek2.Fragment3.Fragment3
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class MainActivity : AppCompatActivity() {
    private val REQ_STORAGE_PERMISSION = 1

    var lastTimeBackPressed : Long = -1;
    val mAdapter = ViewPagerAdapter(this)

    //request code
    val FRAG1_CODE : Int = 0
    val FRAG2_CODE : Int = 1
    val FRAG3_CODE : Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 뷰페이저 설정
        val fragmentList = listOf(Fragment1(), Fragment2(), Fragment3())
        mAdapter.fragments = fragmentList

        //뷰페이저와 어댑터 연결
        view_pager.adapter = mAdapter

        // 탭레이아웃 관리
        TabLayoutMediator(tabs, view_pager) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> {
                    tab.text = "Contact"
                    tab.setIcon(R.drawable.contactbook)
                }
                1 -> {
                    tab.text = "Gallery"
                    tab.setIcon(R.drawable.gallery)
                }

                2 -> {
                    tab.text = "Alcohol"
                    tab.setIcon(R.drawable.beer)
                }
                else -> {
                    tab.text = "Phone Book"
                    tab.setIcon(R.drawable.contactbook)
                }
            }
            view_pager.setCurrentItem(0)
        }.attach()

        checkGalleryPermission(cancel = {showPermissionInfoDialog()}, ok = {})
    }

    /////////////////////////tab2
    private fun checkGalleryPermission(cancel: () -> Unit, ok: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        ) {  // <PERMISSION_DENIED가 반환됨>
            // 이전에 사용자가 앱 권한을 허용하지 않았을 때 -> 왜 허용해야되는지 알람을 띄움
            // shouldShowRequestPermissionRationale메소드는 이전에 사용자가 앱 권한을 허용하지 않았을 때 ture를 반환함
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
            ) {
                //cancel()
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQ_STORAGE_PERMISSION
                )
            }
            // 앱 처음 실행했을 때
            else
            // 권한 요청 알림
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQ_STORAGE_PERMISSION
                )
        } else // 앱에 권한이 허용됨
            ok()
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 2000){
            finish()
            return
        }
        Snackbar.make(view_pager, "뒤로가기 버튼을 한번 더 눌러 종료", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        lastTimeBackPressed = System.currentTimeMillis();
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // () 사용에 대한 사용자의 요청)일 때
            REQ_STORAGE_PERMISSION -> {
                // 요청이 비허용일 때
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    toast("권한 거부 됨")
                    finish()
                }
            }
        }
    }
    // 사용자가 이전에 권한을 거부했을 때 호출된다.
    private fun showPermissionInfoDialog() {
        alert("갤러리 사진을 가져오려면 권한이 필수로 필요합니다", "") {
            yesButton {
                // 권한 요청
                ActivityCompat.requestPermissions(
                        //this@MapsActivity,
                        this@MainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQ_STORAGE_PERMISSION
                )
            }
            noButton {
                toast("권한 거부 됨")
                finish()
            }
        }.show()
    }
    /////////////////////////tab2

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
        when (requestCode) {
            // From Fragment1
            0-> {
                if (resultCode == 1) {
                    Toast.makeText(this, "이름과 번호를 정확히 입력해주세요!", Toast.LENGTH_LONG).show()
                }
                mAdapter.fragments[0].onActivityResult(requestCode, resultCode, data)
                view_pager.adapter = mAdapter
                view_pager.setCurrentItem(requestCode)
            }
            1-> {
                mAdapter.fragments[1].onActivityResult(requestCode, resultCode, data)
                view_pager.adapter = mAdapter
                view_pager.setCurrentItem(requestCode)
            }
            2-> {
                mAdapter.fragments[2].onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}