package com.jeff.architecture_mvvm.view.github

import android.util.SparseArray
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.jeff.architecture_mvvm.R
import com.jeff.architecture_mvvm.databinding.ItemUserInfoBinding
import com.jeff.architecture_mvvm.model.api.vo.UserItem
import com.jeff.architecture_mvvm.view.base.BaseAnyViewHolder
import com.log.JFLog

class GitUserViewHolder(private val binding: ItemUserInfoBinding) : BaseAnyViewHolder<UserItem>(binding.root) {

    private val shapeArray = SparseArray<ShapeAppearanceModel>().also {
        // 未處理
        it.put(0, ShapeAppearanceModel.builder().build())

        // 圓形
        it.put(1, ShapeAppearanceModel.builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build())

        // 菱形
        it.put(2, ShapeAppearanceModel.builder().setAllCorners(CornerFamily.CUT, 0f).setAllCornerSizes(ShapeAppearanceModel.PILL).build())
    }

    init {
        binding.root.setOnClickListener {
            JFLog.d("OnClick: $data")
        }
    }

    override fun updated() {
        // 方法1: 處理ImageView外框, 呈現圓形.
        binding.ivAvatar.shapeAppearanceModel = shapeArray[adapterPosition % 3]

        // user avatar
        Glide.with(itemView.context)
            .load(data?.avatarUrl)
            // 方法2: 使用Glide將圖片轉為圓形
            //.apply(RequestOptions.circleCropTransform())
            .placeholder(R.mipmap.ic_launcher)
            .into(binding.ivAvatar)

        // user login
        data?.login.also {
            binding.tvName.text = it
        }

        // user badge
        if (data?.isSiteAdmin == true) {
            binding.tvBadge.visibility = View.VISIBLE
        } else {
            binding.tvBadge.visibility = View.GONE
        }
    }
}