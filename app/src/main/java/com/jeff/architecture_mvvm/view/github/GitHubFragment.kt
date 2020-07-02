package com.jeff.architecture_mvvm.view.github

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.jeff.architecture_mvvm.databinding.FragmentGithubBinding
import com.jeff.architecture_mvvm.util.autoCleared
import com.jeff.architecture_mvvm.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class GitHubFragment : BaseFragment<FragmentGithubBinding>() {

    private val viewModel by viewModel<GitHubViewModel>()

    private var infoAdapter by autoCleared<UserInfoListAdapter>()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGithubBinding {
        return FragmentGithubBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout.setExpandedTitleColor(Color.YELLOW)
        binding.toolbarLayout.setCollapsedTitleTextColor(Color.BLUE)

        binding.toolbar.setNavigationOnClickListener {

        }

        infoAdapter = UserInfoListAdapter()

        binding.recyclerView.adapter = infoAdapter

        binding.layoutRefresh.setOnRefreshListener {
            viewModel.getUsers()
        }

        viewModel.processing.observe(viewLifecycleOwner, Observer {
            binding.layoutRefresh.isRefreshing = it
        })

        viewModel.userListData.observe(viewLifecycleOwner, Observer {
            infoAdapter.submitList(it)
        })

        viewModel.getUsers()
    }
}
