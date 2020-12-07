package com.jeff.architecture_mvvm.view.github

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jeff.architecture_mvvm.R
import com.jeff.architecture_mvvm.databinding.ItemUserInfoBinding
import com.jeff.architecture_mvvm.model.api.vo.UserItem
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow

class GitUserAdapter : PagingDataAdapter<UserItem, RecyclerView.ViewHolder>(DiffCallback) {

    private val onRequestDrag = BroadcastChannel<RecyclerView.ViewHolder>(Channel.BUFFERED)
    fun onRequestDrag() = onRequestDrag.asFlow()

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<UserItem>() {
            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitUserViewHolder {
        val view = inflateWithLayout(parent, R.layout.item_user_info)
        return GitUserViewHolder(ItemUserInfoBinding.bind(view))
    }

    private fun inflateWithLayout(viewGroup: ViewGroup, @LayoutRes layoutRes: Int): View {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        return layoutInflater.inflate(layoutRes, viewGroup, false)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.also {
            when (holder) {
                is GitUserViewHolder -> holder.bindTo(it)
            }
        }

        setDragDrop(holder)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setDragDrop(holder: RecyclerView.ViewHolder) {
        val view = holder.itemView.findViewById<View>(R.id.iv_avatar)

        view.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                onRequestDrag.offer(holder)
            }

            false
        }
    }
}
