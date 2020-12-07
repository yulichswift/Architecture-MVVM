package com.jeff.architecture_mvvm.view.github

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.jeff.architecture_mvvm.R
import com.jeff.architecture_mvvm.databinding.FragmentGithubBinding
import com.jeff.architecture_mvvm.util.ItemDragDropCallback
import com.jeff.architecture_mvvm.util.autoCleared
import com.jeff.architecture_mvvm.view.base.BaseFragment
import com.log.JFLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GitHubFragment : BaseFragment<FragmentGithubBinding>() {

    private val viewModel by viewModels<GitHubViewModel>()

    private var userAdapter by autoCleared<GitUserAdapter>()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGithubBinding {
        return FragmentGithubBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout.setExpandedTitleColor(Color.YELLOW)
        binding.toolbarLayout.setCollapsedTitleTextColor(Color.BLUE)

        binding.toolbar.setNavigationOnClickListener {

        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_refresh -> {
                    viewModel.refresh()
                    //binding.recyclerView.scrollToPosition(0)
                    true
                }
                R.id.action_trash -> {
                    viewModel.clear()

                    // 在View清空的方法
                    //userAdapter.submitData(lifecycle, PagingData.empty())
                    true
                }
                else -> false
            }
        }

        userAdapter = GitUserAdapter()

        binding.recyclerView.adapter = userAdapter

        val itemDragDropCallback = ItemDragDropCallback()
        val touchHelper = ItemTouchHelper(itemDragDropCallback)
        touchHelper.attachToRecyclerView(binding.recyclerView)

        viewLifecycleOwner.lifecycleScope.launch {
            userAdapter.onRequestDrag().collectLatest {
                touchHelper.startDrag(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            itemDragDropCallback.onRowMoved().collectLatest {
                // But data not moved.
                userAdapter.notifyItemMoved(it.first, it.second)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            itemDragDropCallback.onRowSelected().collectLatest {
                it.itemView.setBackgroundColor(Color.LTGRAY)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            itemDragDropCallback.onRowClear().collectLatest {
                it.itemView.setBackgroundColor(Color.TRANSPARENT)
            }
        }


        binding.layoutRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.processing.observe(viewLifecycleOwner, Observer {
            binding.layoutRefresh.isRefreshing = it
        })

        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            JFLog.e(e)
        }

        lifecycleScope.launch(exceptionHandler) {
            //viewModel.getPageList().collectLatest {
            viewModel.getSimplePageList().collectLatest {
                JFLog.d("Submit: $it")
                userAdapter.submitData(lifecycle, it)
            }
        }

        viewModel.initLoad()
    }
}
