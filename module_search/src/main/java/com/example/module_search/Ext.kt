package com.example.module_search

import androidx.recyclerview.widget.RecyclerView

/**
Created by chene on @date 20-12-6 上午10:43
 **/
fun RecyclerView.isSlideToBottom() =
    (this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset()
            >= this.computeVerticalScrollRange())