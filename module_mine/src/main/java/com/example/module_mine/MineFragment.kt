package com.example.module_mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.view.BaseFragment
import com.example.module_mine.databinding.MineFragmentBinding

/**
Created by chene on @date 20-12-10 下午12:21
 **/
@Route(path = "/mine/fragment")
class MineFragment: BaseFragment() {

    private lateinit var mineBinding: MineFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mineBinding = MineFragmentBinding.inflate(inflater, container, false)
        context ?: return mineBinding.root

        initView()
        initAction()
        subscribe()

        return mineBinding.root
    }

    private fun initView(){

    }

    private fun initAction(){

    }

    private fun subscribe(){

    }
}