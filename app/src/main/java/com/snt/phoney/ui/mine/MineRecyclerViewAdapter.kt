package com.snt.phoney.ui.mine


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.snt.phoney.R
import com.snt.phoney.ui.dating.list.DatingListActivity
import com.snt.phoney.ui.vip.VipActivity
import com.snt.phoney.ui.wallet.WalletActivity
import kotlinx.android.synthetic.main.fragment_mine_footer.view.*
import kotlinx.android.synthetic.main.fragment_mine_header.view.*
import kotlinx.android.synthetic.main.fragment_mine_settings.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 */
class MineRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val settings = ArrayList<Setting>()

    init {
        settings.add(Setting(R.drawable.ic_photo, "相册权限", ""))
        settings.add(Setting(R.drawable.ic_dating, "我的约会", "新消息"))
        settings.add(Setting(R.drawable.ic_wallet, "我的钱包", ""))
        settings.add(Setting(R.drawable.ic_privacy, "隐私设置", ""))
        settings.add(Setting(R.drawable.ic_bind_phone, "绑定手机", ""))
        settings.add(Setting(R.drawable.ic_share, "分享给好友", ""))
        settings.add(Setting(R.drawable.ic_user_protocol, "用户协议", ""))
        settings.add(Setting(R.drawable.ic_clear_cache, "清理缓存", ""))
        settings.add(Setting(R.drawable.ic_about, "关于", ""))
    }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 0
            itemCount - 1 -> 2
            else -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> HeadViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_mine_header, parent, false))
            1 -> SettingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_mine_settings, parent, false))
            else -> FooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_mine_footer, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeadViewHolder) {
            holder.setData()
        } else if (holder is SettingViewHolder) {
            holder.setData(setting = settings[position - 1])
        } else if (holder is FooterViewHolder) {

        }
    }

    override fun getItemCount(): Int = settings.size + 2

    inner class HeadViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        private val context: Context = mView.context
        private val mHead: ImageView = mView.head
        private val mUserName: TextView = mView.username
        private val mVipType: TextView = mView.vipType
        private val mUpgradeVip: TextView = mView.upgradeVip
        private val mEditInfo: TextView = mView.editInfo
        private val mRecentVisitor: TextView = mView.recentVisitor
        private val mRecentVisitorButton: LinearLayout = mView.recentVisitorButton
        private val mFollowMe: TextView = mView.followMe
        private val mFollowMeButton: LinearLayout = mView.followMeButton
        private val mMyDating: TextView = mView.myDating
        private val mMyDatingButton: LinearLayout = mView.myDatingButton

        fun setData() {
            //TODO
            mRecentVisitor.text = "1000"
            mFollowMe.text = "100"
            mMyDating.text = "10000"
            mUpgradeVip.setOnClickListener { context.startActivity(VipActivity.newIntent(context)) }
            mEditInfo.setOnClickListener { }
            mFollowMeButton.setOnClickListener { }
            mRecentVisitorButton.setOnClickListener { }
            mMyDatingButton.setOnClickListener { context.startActivity(DatingListActivity.newIntent(context)) }
        }
    }

    inner class SettingViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        private val context: Context = mView.context
        private val mIcon: ImageView = mView.icon
        private val mTitle: TextView = mView.title
        private val mInfo: TextView = mView.info
        private val mNews: View = mView.news

        fun setData(setting: Setting) {
            mIcon.setImageResource(setting.icon)
            mTitle.text = setting.title
            when (setting.info) {
                null -> {
                    mNews.visibility = View.GONE
                }
                else -> {
                    mInfo.text = setting.info
                    mNews.visibility = View.VISIBLE
                }
            }
            when (setting.icon) {
                R.drawable.ic_photo -> {
                }
                R.drawable.ic_dating -> {
                    mView.setOnClickListener { context.startActivity(DatingListActivity.newIntent(context)) }
                }
                R.drawable.ic_wallet -> {
                    mView.setOnClickListener { context.startActivity(WalletActivity.newIntent(context)) }
                }
                R.drawable.ic_privacy -> {
                }
                R.drawable.ic_bind_phone -> {

                }
                R.drawable.ic_share -> {
                }
                R.drawable.ic_user_protocol -> {
                }
                R.drawable.ic_clear_cache -> {
                }
                R.drawable.ic_about -> {

                }
            }
        }
    }

    inner class FooterViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        init {
            mView.signout.setOnClickListener {

            }
        }
    }
}


data class Setting(val icon: Int, val title: String, var info: String?)