package com.example.madcampweek2.Fragment1

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.madcampweek2.RetroFit.RetrofitClient
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject


class ContactViewModel internal constructor(var activity: Activity) : ViewModel() {
    var contacts : MutableLiveData<ArrayList<PhoneBookData>?> = MutableLiveData<ArrayList<PhoneBookData>?>(BookDataList.getInstance())
    fun getContacts() {
        contactsServer
        Log.i("GetContacts", "서버에서 연락처를 가져왔습니다")
    }

    // is curContacts contains contact?
    private val contactsServer: Unit
        private get() {
            // DB에서 연락처 받아오기
            val contactGetReq = RetrofitClient.instance.apiService.contactsGetAll()
            contactGetReq?.enqueue(object : retrofit2.Callback<ResponseBody?> {
                override fun onResponse(
                    call: retrofit2.Call<ResponseBody?>?,
                    response: retrofit2.Response<ResponseBody?>
                ) {
                    val test = response.body()!!.string()
                    addContactsList(test)

                }

                override fun onFailure(call: retrofit2.Call<ResponseBody?>, t: Throwable) {
                }
            })
        }

    private fun addContactsList(response: String) {
        val array = JSONArray(response)
        for (i in 0 until array.length()) {
            val jresponse : JSONObject = array.getJSONObject(i)
            val item = PhoneBookData(
                jresponse.getString("_id"), jresponse.getString("url"),
                jresponse.getString("name"), jresponse.getString("phoneNum")
            )
            val bookDataList = BookDataList.getInstance()
            bookDataList!!.add(item)
        }
    }
}

class ContactViewModelFactory(
    application: Application?,
    var activity: Activity
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
            return ContactViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}