package com.example.madcampweek2.Fragment1

class BookDataList private constructor() {
    companion object {
        @Volatile private var instance: ArrayList<PhoneBookData>? = arrayListOf()
//        list추가하던 곳

        @JvmStatic fun getInstance(): ArrayList<PhoneBookData>? =
            instance ?: synchronized(this) {
                instance ?: ArrayList<PhoneBookData>().also {
                    instance = it
                }
            }
    }
}
