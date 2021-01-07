package com.example.madcampweek2.Fragment2

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


public class GridItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration(){
    var space : Int = spacing

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space
        } else {
            outRect.top = 0
        }
        //outRect.left = spacing - column * spacing / spanCount
        //outRect.right = (column + 1) * spacing / spanCount
        //outRect.bottom = spacing
    }
}