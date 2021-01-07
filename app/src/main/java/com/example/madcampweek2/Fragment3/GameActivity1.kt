package com.example.madcampweek2.Fragment3

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.madcampweek2.R
import kotlinx.android.synthetic.main.activity_game1.*
import kotlinx.android.synthetic.main.activity_game1_list_item.view.*
import org.jetbrains.anko.toast

class GameActivity1 : AppCompatActivity() {
    private var start_flag : Int = 0
    private val cardset_idx_init = mapOf(
        "A" to 0, "2" to 1, "3" to 2, "4" to 3,
        "5" to 4, "6" to 5, "7" to 6, "8" to 7,
        "9" to 8, "10" to 9, "J" to 10, "Q" to 11,
        "K" to 12, "Joker" to 13
    )
    private var remain_cardset_init = listOf(4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2)
    private var remain_cardset = remain_cardset_init.toMutableList()
    private var cardset_idx = cardset_idx_init.toMutableMap()
    private var cardkeys = cardset_idx.keys
    private var remainnum = 54

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game1)
        card_A.card_text.text="A"
        card_2.card_text.text="2"
        card_3.card_text.text="3"
        card_4.card_text.text="4"
        card_5.card_text.text="5"
        card_6.card_text.text="6"
        card_7.card_text.text="7"
        card_8.card_text.text="8"
        card_9.card_text.text="9"
        card_10.card_text.text="10"
        card_J.card_text.text="J"
        card_Q.card_text.text="Q"
        card_K.card_text.text="K"
        card_Joker.card_text.text="Joker"

        start_text.bringToFront()
        card_back_view.visibility = View.INVISIBLE
        // scale card_back_view
        card_back_view.layoutParams.height = 548
        card_back_view.layoutParams.width = 292

        // card click
        remindcards.setOnClickListener{
            if(start_flag == 0){
                if(isAllConditionFilled()){
                    remain_cardset = remain_cardset_init.toMutableList()
                    cardset_idx = cardset_idx_init.toMutableMap()
                    cardkeys = cardset_idx.keys
                    remainnum = 54
                    start_text.visibility = View.INVISIBLE
                    card_back_view.visibility = View.VISIBLE
                    picked_card_text.text = ""
                    picked_con_text.text=""
                    start_flag = 1
                }
                else{
                    toast("모든 Condition을 채워주세요")
                }
            }
            else{
                val picked_card = throwCard().toList()
                picked_card_text.text = picked_card[1].toString()
                val idx = cardset_idx_init[picked_card[1]]
                if(idx != null) {
                    val condition = scroll_layout.getChildAt(idx)
                    val cond_text = condition.condition_text.text.toString()
                    picked_con_text.text = (cond_text)
                }
                else{
                    picked_con_text.text=""
                }
                if(picked_card[0] == 0) {
                    card_back_view.visibility = View.INVISIBLE
                    start_text.text="끝!\n터치하면 재시작!"
                    start_text.visibility = View.VISIBLE
                    start_flag = 0
                }
            }
        }

        //resume button
        resume_btn.setOnClickListener{
            start_text.text="터치하면 시작!"
            card_back_view.visibility = View.INVISIBLE
            start_text.visibility = View.VISIBLE
            start_flag = 0
            picked_card_text.text = ""
            picked_con_text.text=""
        }
        //scaleImage(card_back_view)

        random_cond_btn.setOnClickListener{
            setRandomCondition()
        }

        delete_btn.setOnClickListener{
            deleteCondition()
        }
    }

    private fun throwCard() : Pair<Int, String>{
        if(remainnum == 0){
            return Pair(0, "")
        }
        while(true) {
            var pick_card: String = cardkeys.shuffled().take(1)[0]

            if (remain_cardset[cardset_idx[pick_card]!!] == 0) {
                cardset_idx.remove(pick_card)
                cardkeys = cardset_idx.keys
            } else {
                var idx: Int = cardset_idx[pick_card]!!
                var temp: Int = remain_cardset[idx] - 1
                remainnum -= 1
                remain_cardset.set(idx, temp)
                return Pair(remainnum, pick_card)
            }
        }
    }

    private val conditions = listOf(
        "청바지", "모자", "후드", "안경", "귀걸이", "솔로", "커플", "군필", "과대", "모두",
        "슬리퍼", "운동화", "16이상", "동잠러", "댄동", "방금 웃은 사람", "단발", "장발"
    )

    fun setRandomCondition(){
        //var pick_cond: List<String> = conditions.shuffled().take(14)
        card_A.condition_text.setText(conditions[0])
        card_2.condition_text.setText(conditions[1])
        card_3.condition_text.setText(conditions[2])
        card_4.condition_text.setText(conditions[3])
        card_5.condition_text.setText(conditions[4])
        card_6.condition_text.setText(conditions[5])
        card_7.condition_text.setText(conditions[6])
        card_8.condition_text.setText(conditions[7])
        card_9.condition_text.setText(conditions[8])
        card_10.condition_text.setText(conditions[9])
        card_J.condition_text.setText(conditions[10])
        card_Q.condition_text.setText(conditions[11])
        card_K.condition_text.setText(conditions[12])
        card_Joker.condition_text.setText(conditions[13])

    }

    fun deleteCondition(){
        card_A.condition_text.setText("")
        card_2.condition_text.setText("")
        card_3.condition_text.setText("")
        card_4.condition_text.setText("")
        card_5.condition_text.setText("")
        card_6.condition_text.setText("")
        card_7.condition_text.setText("")
        card_8.condition_text.setText("")
        card_9.condition_text.setText("")
        card_10.condition_text.setText("")
        card_J.condition_text.setText("")
        card_Q.condition_text.setText("")
        card_K.condition_text.setText("")
        card_Joker.condition_text.setText("")
    }

    fun isAllConditionFilled() : Boolean{
        val card_a : String = card_A.condition_text.text.toString()
        val card_2 : String = card_2.condition_text.text.toString()
        val card_3 : String = card_3.condition_text.text.toString()
        val card_4 : String = card_4.condition_text.text.toString()
        val card_5 : String = card_5.condition_text.text.toString()
        val card_6 : String = card_6.condition_text.text.toString()
        val card_7 : String = card_7.condition_text.text.toString()
        val card_8 : String = card_8.condition_text.text.toString()
        val card_9 : String = card_9.condition_text.text.toString()
        val card_10 : String = card_10.condition_text.text.toString()
        val card_J : String = card_J.condition_text.text.toString()
        val card_Q : String = card_Q.condition_text.text.toString()
        val card_K : String = card_K.condition_text.text.toString()
        val card_Joker : String = card_Joker.condition_text.text.toString()
        if(card_a == "" || card_2 == "" || card_3 == "" || card_4 == "" ||
            card_5 == "" || card_6 == "" || card_7 == "" || card_8 == "" ||
            card_9 == "" || card_10 == "" || card_J == "" || card_Q == "" ||
            card_K == "" || card_Joker == ""){
            return false
        }
        return true
    }

}