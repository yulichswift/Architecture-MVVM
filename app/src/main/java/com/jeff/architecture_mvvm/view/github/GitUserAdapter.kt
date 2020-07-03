package com.jeff.architecture_mvvm.view.github

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jeff.architecture_mvvm.R
import com.jeff.architecture_mvvm.databinding.ItemUserInfoBinding
import com.jeff.architecture_mvvm.model.api.vo.UserItem

class GitUserAdapter : PagedListAdapter<UserItem, RecyclerView.ViewHolder>(DiffCallback) {

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

    private fun inflateView(viewGroup: ViewGroup, @LayoutRes viewType: Int): View {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        return layoutInflater.inflate(viewType, viewGroup, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitUserViewHolder {
        val view = inflateView(parent, R.layout.item_user_info)
        return GitUserViewHolder(ItemUserInfoBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.also {
            when (holder) {
                is GitUserViewHolder -> holder.bindTo(it)
            }
        }
    }
}
