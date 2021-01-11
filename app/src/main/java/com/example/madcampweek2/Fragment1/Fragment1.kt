package com.example.madcampweek2.Fragment1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.madcampweek2.*
import kotlinx.android.synthetic.main.fragment_1.*

class Fragment1 : Fragment() {
    private var bookDataList: ArrayList<PhoneBookData>? = BookDataList.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_1, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener {
            if (bookDataList!!.isEmpty()) {
                getPhoneNumbers()
                phone_book_list.adapter = bookDataList?.let { it ->
                    context?.let { it1 ->
                        PhoneBookListAdapter(it1, it)
                    }
                }
            } else {
                // Change Activity.
                val intent = Intent(context, AddActivity::class.java)
                activity?.startActivityForResult(intent, 0)
            }
        }

        // 초기 연락처 load
//        if (bookDataList!!.isEmpty()) {
//            VolleyService.getContactVolley(context as Activity)
//        }
        bookDataList = BookDataList.getInstance()
        phone_book_list.adapter = bookDataList?.let { it ->
            context?.let { it1 ->
                PhoneBookListAdapter(it1, it)
            }
        }
        phone_book_list.layoutManager = LinearLayoutManager(context)

        search_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var searchList: ArrayList<PhoneBookData> = arrayListOf()
                bookDataList?.forEachIndexed { index, item ->
                    if (item.name!!.contains(s, true)) {
                        searchList.add(item)
                    }
                }
                // adapting recyclerview.
                if (searchList.isEmpty() || s.equals("")) {
                    phone_book_list.adapter = bookDataList?.let { it ->
                        context?.let { it1 -> PhoneBookListAdapter(it1, it) }
                    }
                    phone_book_list.layoutManager = LinearLayoutManager(context)
                } else {
                    phone_book_list.adapter = context?.let { it1 -> PhoneBookListAdapter(it1, searchList) }
                    phone_book_list.layoutManager = LinearLayoutManager(context)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        bookDataList = BookDataList.getInstance()
        phone_book_list.layoutManager = LinearLayoutManager(context)
        phone_book_list.adapter = bookDataList?.let { it ->
            context?.let { it1 -> PhoneBookListAdapter(it1, it) }
        }
    }

    fun getPhoneNumbers(){
        // 결과목록 미리 정의
        val list = BookDataList.getInstance()
        // 1. 전화번호 Uri
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        // 2.1 전화번호에서 가져올 컬럼 정의
        val projections = arrayOf(ContactsContract.CommonDataKinds.Photo.PHOTO_URI
                , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                , ContactsContract.CommonDataKinds.Phone.NUMBER)
        // 2.2 조건 정의
        var wheneClause:String? = null
        var whereValues:Array<String>? = null

        val optionSort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " asc"
        // 3. 테이블에서 주소록 데이터 쿼리
        context?.run{
            val cursor = contentResolver.query(phoneUri, projections, wheneClause, whereValues, optionSort)
            // 4. 반복문으로 아이디와 이름을 가져오면서 전화번호 조회 쿼리를 한번 더 돌린다.
            while(cursor?.moveToNext()?:false) {
                val photoURI = cursor?.getString(0)
                val name = cursor?.getString(1)
                val number = cursor?.getString(2)
                // 개별 전화번호 데이터 생성
                val phone = PhoneBookData(null ,photoURI, name, number)
                // 결과목록에 더하기
                list?.add(phone)
            }
        }
    }
}
