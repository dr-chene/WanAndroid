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
import com.example.lib_net.bean.NetResult
import com.example.lib_net.bean.doFailure
import com.example.lib_net.bean.doSuccess
import com.example.lib_net.loginCheck
import com.example.lib_net.util.MmkvUtil
import com.example.module_mine.databinding.MineFragmentBinding
import com.example.module_mine.repository.CoinRepository
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
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
    private var coin: com.example.share_coin.Coin? = null
    private var getCoinJob: Job? = null
    private val aRouter by lazy {
        ARouter.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mineBinding = MineFragmentBinding.inflate(inflater, container, false)
        context ?: return mineBinding.root

        initAction()

        return mineBinding.root
    }

    override fun onResume() {
        super.onResume()
        userShow()
    }

    private fun initAction() {
        mineBinding.mineHead.ivAvatar.setOnClickListener {
            if (!MmkvUtil.isLogin()) {
                login()
            }
        }
        mineBinding.mineContent.mineSetting.root.setOnClickListener {
            setting()
        }
        mineBinding.mineHead.tvRankHead.setOnClickListener {
            loginCheck { coinRank() }
        }
        mineBinding.mineHead.tvRank.setOnClickListener {
            loginCheck { coinRank() }
        }
        mineBinding.mineContent.mineCoin.root.setOnClickListener {
            loginCheck { coinDetail() }
        }
        mineBinding.mineContent.mineArticle.root.setOnClickListener {
            loginCheck {
                ARouter.getInstance()
                    .build("/cid/activity")
                    .withString("cate", "myShare")
                    .withString("cid", "-1")
                    .navigation()
            }
        }
        mineBinding.mineContent.mineCollect.root.setOnClickListener {
            loginCheck {
                ARouter.getInstance().build("/article_collect/activity").navigation()
            }
        }
        mineBinding.mineContent.mineWeb.root.setOnClickListener {
            loginCheck {
                ARouter.getInstance().build("/web_collect/activity").navigation()
            }
        }
    }

    private fun login() {
        aRouter.build("/login/activity").navigation()
    }

    private fun setting() {
        aRouter.build("/setting/activity").navigation()
    }

    private fun coinRank() {
        aRouter.build("/coin/rank/activity").navigation()
    }

    private fun coinDetail() {
        aRouter.build("/coin_detail/activity").navigation()
    }

    private fun userShow() {
        user = mmkv.decodeParcelable("user", User::class.java)
        coin = mmkv.decodeParcelable("coin", com.example.share_coin.Coin::class.java)
        if (user != null && coin == null) {
            getCoinJob = getCoin()
        } else if (user == null) {
            getCoinJob?.cancel()
        }
        mineBinding.user = user
        mineBinding.coin = coin
        mineBinding.executePendingBindings()
    }

    private fun getCoin() = netRequest(coinRepository.getCoin()) {
        mineBinding.coin = it
        mmkv.encode("coin", it)
        mineBinding.executePendingBindings()
        "coin get success".showToast()
    }

    private inline fun <reified T> netRequest(
        request: Flow<NetResult<T>>,
        crossinline success: (T) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        request.collectLatest {
            withContext(Dispatchers.Main) {
                it.doSuccess { r ->
                    success.invoke(r)
                }
                it.doFailure { t ->
                    t?.showToast()
                }
            }
        }
    }
}