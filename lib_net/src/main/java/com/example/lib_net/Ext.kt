package com.example.lib_net

import androidx.recyclerview.widget.RecyclerView

/**
Created by chene on @date 20-12-11 下午8:44
 **/
fun RecyclerView.isSlideToBottom() =
    (this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset()
            >= this.computeVerticalScrollRange())

fun RecyclerView.loadAction(load: () -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING && recyclerView.isSlideToBottom()) {
                load.invoke()
            }
        }
    })
}