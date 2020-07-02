package com.jeff.architecture_mvvm.view.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAnyViewHolder<M : Any>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected var data: M? = null

    fun bindTo(bind: M?) {
        data = bind
        updated()
    }

    abstract fun updated()
}