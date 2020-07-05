package com.jeff.architecture_mvvm.view.github

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.jeff.architecture_mvvm.R
import com.jeff.architecture_mvvm.databinding.FragmentGithubBinding
import com.jeff.architecture_mvvm.util.autoCleared
import com.jeff.architecture_mvvm.view.base.BaseFragment
import com.log.JFLog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GitHubFragment : BaseFragment<FragmentGithubBinding>() {

    private val viewModel by viewModel<GitHubViewModel>()

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

        binding.layoutRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.processing.observe(viewLifecycleOwner, Observer {
            binding.layoutRefresh.isRefreshing = it
        })

        lifecycleScope.launch {
            //viewModel.getPageList().collectLatest {
            viewModel.getSimplePageList().collectLatest {
                JFLog.d("Submit: $it")
                userAdapter.submitData(lifecycle, it)
            }
        }

        viewModel.initLoad()
    }
}
