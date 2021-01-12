package com.example.madcampweek2.Fragment3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcampweek2.Fragment1.PostAdapter
import com.example.madcampweek2.R
import kotlinx.android.synthetic.main.fragment_1.*
import kotlinx.android.synthetic.main.fragment_3.*

class Fragment3 : Fragment() {

    private var postDataList: ArrayList<PostData>? = PostDataList.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postDataList = PostDataList.getInstance()
        postDataList?.sort()
        post_list.adapter = context?.let { postDataList?.let {
                it1 -> PostAdapter(it, it1) } }
        post_list.layoutManager = LinearLayoutManager(context)
    }
}