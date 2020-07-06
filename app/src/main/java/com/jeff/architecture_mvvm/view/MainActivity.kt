package com.jeff.architecture_mvvm.view

import com.jeff.architecture_mvvm.databinding.ActivityMainBinding
import com.jeff.architecture_mvvm.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}