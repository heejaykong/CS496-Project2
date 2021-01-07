package com.example.madcampweek2.Fragment1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.example.madcampweek2.R
import kotlinx.android.synthetic.main.phonebook_item.view.*

class PhoneBookViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var view : View = v

    fun bind(item: PhoneBookData) {
        view.name.text = item.name
    }
}

class PhoneBookListAdapter(val mContext: Context, val itemList: List<PhoneBookData>) : RecyclerView.Adapter<PhoneBookViewHolder>() {
    override fun getItemCount() : Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneBookViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.phonebook_item,
            parent,
            false
        )
        return PhoneBookViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: PhoneBookViewHolder, position: Int) {

        val ITEM_CODE = 1
        val item = itemList[position]
        holder.apply {
            bind(item)
        }

        // call button click event
        holder.view.call_button.setOnClickListener{
            var phoneNumber = "tel:"
            phoneNumber += item.number?.get(0)
            phoneNumber += item.number?.get(1)
            phoneNumber += item.number?.get(2)
            phoneNumber += "-"
            phoneNumber += item.number?.get(3)
            phoneNumber += item.number?.get(4)
            phoneNumber += item.number?.get(5)
            phoneNumber += item.number?.get(6)
            phoneNumber += "-"
            phoneNumber += item.number?.get(7)
            phoneNumber += item.number?.get(8)
            phoneNumber += item.number?.get(9)
            phoneNumber += item.number?.get(10)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(phoneNumber))
            mContext.startActivity(intent)
        }

        // item click event
        holder.itemView.setOnClickListener{

            // Set context, intent.
            val intent = Intent(mContext, ItemActivity::class.java)

            // Set variables for bundle
            val name = item.name
            val number = item.number

            // Bundle을 통해서 전달
            val bundle = Bundle()
            bundle.putString("name", name)
            bundle.putString("number", number)
            bundle.putInt("position", position)

            intent.putExtras(bundle)    // intent 객체에 Bundle을 저장

            startActivityForResult(mContext as Activity, intent, 0, bundle)
        }
    }
}


