package com.jeff.architecture_mvvm.view.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<M : Any>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected var storedData: M? = null

    open fun bindTo(data: M?) {
        storedData = data
    }
}