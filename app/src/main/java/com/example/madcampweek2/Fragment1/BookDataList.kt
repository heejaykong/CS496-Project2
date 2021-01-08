package com.example.madcampweek2.Fragment1

class BookDataList private constructor() {
    companion object {
        @Volatile private var instance: ArrayList<PhoneBookData>? = arrayListOf(
//            PhoneBookData("김철환","01022591802"),
//            PhoneBookData("신정윤","01043563523"),
//            PhoneBookData("심건희","01023523422"),
//            PhoneBookData("이정인","01063454534"),
//            PhoneBookData("한다진","01022725476"),
//            PhoneBookData("김현아","01025675643"),
//            PhoneBookData("함창수","01078462423"),
//            PhoneBookData("류석영 교수님","01024673463")
        )

        @JvmStatic fun getInstance(): ArrayList<PhoneBookData>? =
            instance ?: synchronized(this) {
                instance ?: ArrayList<PhoneBookData>().also {
                    instance = it
                }
            }
    }
}