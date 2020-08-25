package com.jeff.architecture_mvvm.view.github2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.jeff.architecture_mvvm.R
import com.jeff.architecture_mvvm.databinding.FragmentViewpagerBinding
import com.jeff.architecture_mvvm.util.autoCleared
import com.jeff.architecture_mvvm.view.base.BaseFragment
import com.jeff.architecture_mvvm.view.github.GitHubViewModel
import com.log.JFLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewPagerFragment : BaseFragment<FragmentViewpagerBinding>() {

    private val viewModel by viewModels<GitHubViewModel>()

    private var userAdapter by autoCleared<GitUserAdapter2>()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentViewpagerBinding {
        return FragmentViewpagerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userAdapter = GitUserAdapter2()

        initViewPager()

        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            JFLog.e(e)
        }

        lifecycleScope.launch(exceptionHandler) {
            viewModel.getSimplePageList().collectLatest {
                JFLog.d("Submit: $it")
                userAdapter.submitData(lifecycle, it)
            }
        }

        viewModel.initLoad()
    }

    private fun initViewPager() {
        (binding.viewpager.getChildAt(0) as RecyclerView).apply {
            val padding = resources.getDimensionPixelOffset(R.dimen.halfPageMargin) +
                    resources.getDimensionPixelOffset(R.dimen.peekOffset)
            setPadding(padding, 0, padding, 0)
            clipToPadding = false
        }

        binding.viewpager.adapter = userAdapter
    }
}
