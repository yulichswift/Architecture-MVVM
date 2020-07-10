package com.jeff.architecture_mvvm.view.github

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jeff.architecture_mvvm.R
import com.jeff.architecture_mvvm.databinding.ItemUserInfoBinding
import com.jeff.architecture_mvvm.model.api.vo.UserItem

class GitUserAdapter : PagingDataAdapter<UserItem, RecyclerView.ViewHolder>(DiffCallback) {

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
    }
}
