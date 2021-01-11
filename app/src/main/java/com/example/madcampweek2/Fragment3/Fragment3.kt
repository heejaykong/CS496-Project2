package com.example.madcampweek2.Fragment3

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.madcampweek2.R
import kotlinx.android.synthetic.main.fragment_3.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment3.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        game3.setOnClickListener {
            Toast.makeText(context, "구현 예정입니다.", Toast.LENGTH_LONG).show()
        }

        game4.setOnClickListener {
            val intent = Intent(context, GameActivity4::class.java)
            activity?.startActivityForResult(intent, 2)
        }

        game5.setOnClickListener {
            Toast.makeText(context, "구현 예정입니다.", Toast.LENGTH_LONG).show()
        }

        game6.setOnClickListener {
            Toast.makeText(context, "구현 예정입니다.", Toast.LENGTH_LONG).show()
        }
    }
}
