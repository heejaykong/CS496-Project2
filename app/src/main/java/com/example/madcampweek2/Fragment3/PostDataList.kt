package com.example.madcampweek2.Fragment3

import com.example.madcampweek2.Fragment1.PhoneBookData

class PostDataList private constructor() {
    companion object {
        @Volatile private var instance: ArrayList<PostData>? = arrayListOf(
            PostData(null, "김철환", "http://192.249.18.208:3000/images/uploads/60dcafbcc0f74be0c1625dbdfd4a4d78.JPEG",
                "2020-01-11", "아름관 앞에서", "http://192.249.18.208:3000/images/uploads/540c4cc8259a96d90935101fa1962ba3.JPEG",
                7, false),
            PostData(null, "이정인", "http://192.249.18.208:3000/images/uploads/ba079906a0f49c3247ad8d76e0a2385a.JPEG",
                "2020-01-10", "아빠와 아들\n#KAIST #아름관 #선팔맞팔", "http://192.249.18.208:3000/images/uploads/09b1dda64eae7e6e5554d2f7818fddb1.JPEG",
                104, true),
            PostData(null, "공희재", "http://192.249.18.208:3000/images/uploads/73008a4e0a35ee8e7cdd6b8822f4dbf0.JPEG",
                "2020-01-09", "Zzz...\n#길냥이", "http://192.249.18.208:3000/images/uploads/1098ea28c0236bb3d9baaa87edfda81f.JPEG",
                41, false)
        )

        @JvmStatic fun getInstance(): ArrayList<PostData>? =
            instance ?: synchronized(this) {
                instance ?: ArrayList<PostData>().also {
                    instance = it
                }
            }
    }
}