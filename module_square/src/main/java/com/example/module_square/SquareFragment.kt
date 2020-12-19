package com.example.module_square

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.view.BaseFragment
import com.example.module_square.databinding.SquareFragmentBinding
import com.example.module_square.fragment.TabSquareFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
Created by chene on @date 20-12-13 下午10:28
 **/
@Route(path = "/square/fragment")
class SquareFragment : BaseFragment() {

    private lateinit var squareBinding: SquareFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        squareBinding = SquareFragmentBinding.inflate(inflater, container, false)
        context ?: return squareBinding.root

        initView()
        initAction()
        subscribe()

        return squareBinding.root
    }

    private fun initView() {
        squareBinding.squareViewPage.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> TabSquareFragment(FRAGMENT_TYPE_SQUARE)
                    else -> TabSquareFragment(FRAGMENT_TYPE_QA)
                }
            }
        }
        squareBinding.apply {
            TabLayoutMediator(squareTopTab, squareViewPage) { tab, position ->
                when (position) {
                    0 -> tab.text = resources.getText(R.string.square_tab_square)
                    else -> tab.text = resources.getText(R.string.square_tab_qa)
                }
            }.attach()
        }
        squareBinding.squareViewPage.isUserInputEnabled = false
    }

    private fun initAction() {

    }

    private fun subscribe() {

    }

    companion object {
        const val FRAGMENT_TYPE_SQUARE = 0
        const val FRAGMENT_TYPE_QA = 1
    }
}