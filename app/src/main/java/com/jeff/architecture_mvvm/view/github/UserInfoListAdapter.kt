package com.jeff.architecture_mvvm.view.github

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jeff.architecture_mvvm.R
import com.jeff.architecture_mvvm.databinding.ItemUserInfoBinding
import com.jeff.architecture_mvvm.model.api.vo.UserItem

class UserInfoListAdapter : PagedListAdapter<UserItem, RecyclerView.ViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<UserItem>() {
            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_user_info, parent, false)
        return UserInfoViewHolder(ItemUserInfoBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.also {
            when (holder) {
                is UserInfoViewHolder -> holder.bindTo(it)
            }
        }
    }
}
