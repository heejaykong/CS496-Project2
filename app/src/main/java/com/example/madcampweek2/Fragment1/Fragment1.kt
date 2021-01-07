package com.example.madcampweek2.Fragment1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcampweek2.*
import kotlinx.android.synthetic.main.fragment_1.*

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1 : Fragment() {
    private var bookDataList : ArrayList<PhoneBookData>? = BookDataList.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_1, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fab.setOnClickListener {
            // Change Activity.
            val intent = Intent(context, AddActivity::class.java)
            activity?.startActivityForResult(intent, 0)
        }

        phone_book_list.adapter = bookDataList?.let { it ->
            context?.let { it1 -> PhoneBookListAdapter(it1, it) }}
        phone_book_list.layoutManager = LinearLayoutManager(context)

        search_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var searchList : ArrayList<PhoneBookData> = arrayListOf()
                bookDataList?.forEachIndexed {index, item ->
                    if (item.name!!.contains(s, true)) {
                        searchList.add(item)
                    }
                }
                // adapting recyclerview.
                if (searchList.isEmpty() || s.equals("")) {
                    phone_book_list.adapter = bookDataList?.let { it ->
                        context?.let { it1 -> PhoneBookListAdapter(it1, it) }}
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
            context?.let { it1 -> PhoneBookListAdapter(it1, it) } }
    }
}
