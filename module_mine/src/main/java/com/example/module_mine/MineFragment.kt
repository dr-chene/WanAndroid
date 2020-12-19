package com.example.module_mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
import com.example.module_mine.databinding.AlertInputBinding
import com.example.module_mine.databinding.MineFragmentBinding
import com.example.module_mine.repository.CoinRepository
import com.example.module_mine.repository.ShareCollectRepository
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
    private val shareCollectRepository by inject<ShareCollectRepository>()
    private val aRouter by lazy {
        ARouter.getInstance()
    }
    private val shareArticleView by lazy {
        AlertInputBinding.bind(
            LayoutInflater.from(requireContext()).inflate(R.layout.alert_input, null)
        )
    }
    private val shareArticle by lazy {
        AlertDialog.Builder(requireContext()).apply {
            setView(shareArticleView.root)
        }.create()
    }

    private val collectArticleView by lazy {
        AlertInputBinding.bind(
            LayoutInflater.from(requireContext()).inflate(R.layout.alert_input, null)
        ).apply {
            alertTitle.text = resources.getText(R.string.item_collect)
            alertInputLayoutAuthor.visibility = View.VISIBLE
            alertConfirmBtn.setText(R.string.alert_confirm_btn_collect)
        }
    }
    private val collectArticle by lazy {
        AlertDialog.Builder(requireContext()).apply {
            setView(collectArticleView.root)
        }.create()
    }

    private val collectWebView by lazy {
        AlertInputBinding.bind(
            LayoutInflater.from(requireContext()).inflate(R.layout.alert_input, null)
        ).apply {
            alertTitle.text = resources.getText(R.string.item_web)
            alertInputLayoutTitle.hint = "name"
            alertConfirmBtn.setText(R.string.alert_confirm_btn_collect)
        }
    }
    private val collectWeb by lazy {
        AlertDialog.Builder(requireContext()).apply {
            setView(collectWebView.root)
        }.create()
    }

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
        initAlertAction(shareArticleView, shareArticle) { title, link, _ ->
            CoroutineScope(Dispatchers.Main).launch {
                shareArticle(title, link)
            }
        }
        initAlertAction(collectArticleView, collectArticle) { title, link, author ->
            CoroutineScope(Dispatchers.Main).launch {
                collectArticle(title, author, link)
            }
        }
        initAlertAction(collectWebView, collectWeb) { title, link, _ ->
            CoroutineScope(Dispatchers.Main).launch {
                collectWeb(title, link)
            }
        }
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
            loginCheck { shareArticle.show() }
        }
        mineBinding.mineContent.mineCollect.root.setOnClickListener {
            loginCheck { collectArticle.show() }
        }
        mineBinding.mineContent.mineWeb.root.setOnClickListener {
            loginCheck { collectWeb.show() }
        }
    }

    private fun subscribe() {

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

    private suspend fun shareArticle(title: String, link: String) = netRequest(
        shareCollectRepository.shareArticle(title, link)
    ) {
        shareArticle.dismiss()
        "文章分享成功".showToast()
    }

    private suspend fun collectArticle(title: String, author: String, link: String) = netRequest(
        shareCollectRepository.collectArticle(title, author, link)
    ) {
        collectArticle.dismiss()
        "文章收藏成功".showToast()
    }

    private suspend fun collectWeb(name: String, link: String) = netRequest(
        shareCollectRepository.collectWeb(name, link)
    ) {
        collectWeb.dismiss()
        "网站收藏成功".showToast()
    }

    private fun initAlertAction(
        view: AlertInputBinding,
        alert: AlertDialog,
        confirm: (title: String, link: String, author: String) -> Unit
    ) {
        view.alertClose.setOnClickListener {
            alert.dismiss()
        }
        view.alertConfirmBtn.setOnClickListener {
            val title = view.alertInputTitle.text.toString()
            val link = view.alertInputLink.text.toString()
            val author = view.alertInputAuthor.text.toString()
            confirm.invoke(title, link, author)
        }
    }
}