package com.example.module_mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.bean.User
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseFragment
import com.example.lib_net.MmkvUtil
import com.example.lib_net.doFailure
import com.example.lib_net.doSuccess
import com.example.module_mine.bean.Coin
import com.example.module_mine.databinding.MineFragmentBinding
import com.example.module_mine.repository.CoinRepository
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject

/**
Created by chene on @date 20-12-10 下午12:21
 **/
@Route(path = "/mine/fragment")
class MineFragment : BaseFragment() {

    private lateinit var mineBinding: MineFragmentBinding
    private val coinRepository by inject<CoinRepository>()
    private val mmkv = MMKV.defaultMMKV()
    private var user: User? = null
    private var coin: Coin? = null
    private var getCoinJob: Job? = null

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

    override fun onResume() {
        super.onResume()
        userShow()
    }

    private fun initView() {

    }

    private fun initAction() {
        mineBinding.mineHead.ivAvatar.setOnClickListener {
            if (!MmkvUtil.isLogin()) {
                login()
            }
        }
        mineBinding.mineContent.setting.root.setOnClickListener {
            setting()
        }
    }

    private fun subscribe() {

    }

    private fun login() {
        ARouter.getInstance().build("/login/activity").navigation()
    }

    private fun setting() {
        ARouter.getInstance().build("/setting/activity").navigation()
    }

    private fun userShow() {
        user = mmkv.decodeParcelable("user", User::class.java)
        coin = mmkv.decodeParcelable("coin", Coin::class.java)
        if (user != null && coin == null) {
            getCoinJob = getCoin()
        } else if (user == null) {
            getCoinJob?.cancel()
        }
        mineBinding.user = user
        mineBinding.coin = coin
        mineBinding.executePendingBindings()
    }

    private fun getCoin() = CoroutineScope(Dispatchers.IO).launch {
        coinRepository.getCoin().collectLatest {
            withContext(Dispatchers.Main) {
                it.doSuccess { coin ->
                    mineBinding.coin = coin
                    mmkv.encode("coin", coin)
                    mineBinding.executePendingBindings()
                    "coin get success".showToast()
                }
                it.doFailure { s ->
                    s?.showToast()
                }
            }
        }
    }
}