package com.example.madcampweek2.Fragment3

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.parseColor
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import java.lang.Float.max

class RouletteView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle){

    var radOfText = 0f
    var angle = 360f
    var startAngle = 0f
    private var itemList: ArrayList<String> = ArrayList()
    val pos_center : Float = 250f
    var velocity = 2f

    private var paintMain : Paint = Paint().apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = parseColor("#F6EEE2")
    }

    private var paintRound : Paint = Paint().apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = parseColor("#443014")
    }

    private var paintHand : Paint = Paint().apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = Color.BLACK
    }

    private var paintA : Paint = Paint().apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = parseColor("#EFB4C1")
    }

    private var paintB : Paint = Paint().apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = parseColor("#F0C7AB")
    }

    private var paintC : Paint = Paint().apply {
        isFilterBitmap = true
        isAntiAlias = true
        color = parseColor("#96B1D0")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        canvas?.drawCircle(pos_center, pos_center, 205f, paintRound)

        canvas?.drawCircle(pos_center, pos_center, 200f, paintMain)

        val paintList = listOf<Paint>(paintA, paintB, paintC)

        paintHand.textSize = 30f

        if (itemList.size!=0){
            itemList.forEachIndexed { index, item ->
                val radius : Double = angleToradius((startAngle+angle*(index+0.5)).toFloat())
                canvas?.drawArc(pos_center-200, pos_center-200,
                    pos_center+200, pos_center+200, startAngle+angle*index,
                    angle, true, paintList[index.rem(3)])
                canvas?.drawText(item, (pos_center + radOfText*Math.cos(radius)).toFloat(),
                    (pos_center + radOfText*Math.sin(radius)).toFloat(), paintHand)
            }
        }

        canvas?.drawArc(pos_center-40f, pos_center-220f,
            pos_center+40f, pos_center-80f
            , 255f, 30f,true, paintHand)


    }

    fun manageCircle (itemList : ArrayList<String>) {
        this.itemList = itemList
        this.angle = 360f/itemList.size
        this.radOfText = ((200/(1+1/Math.sin(angleToradius(this.angle/2))))
                /Math.sin(angleToradius(this.angle/2))).toFloat()
        invalidate()
    }

    fun angleToradius (angle: Float) : Double {
        return (angle*Math.PI/180).toDouble()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateStartAngle(){
        startAngle += velocity
        if (startAngle > 360) {
            startAngle -= 360
        }
        velocity = max(velocity-0.005f*velocity,0f)
        if (velocity < 5) {
            velocity = max(velocity - 0.5f, 0f)
        }else if (velocity < 4) {
            velocity = max(velocity - 0.4f, 0f)
        }else if (velocity < 3) {
            velocity = max(velocity - 0.3f, 0f)
        }else if (velocity < 2) {
            velocity = max(velocity - 0.2f, 0f)
        }else if (velocity < 1) {
            velocity = max(velocity - 0.1f, 0f)
        }else {
            velocity = max(velocity-0.005f*velocity,0f)
        }
        invalidate()
    }

}