package com.snt.phoney.ui.main.mine


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.AmountInfo
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.user.UserActivity
import com.snt.phoney.widget.PhotoFlowAdapter
import kotlinx.android.synthetic.main.fragment_mine_footer.view.*
import kotlinx.android.synthetic.main.fragment_mine_list_header.view.*
import kotlinx.android.synthetic.main.fragment_mine_list_no_photo.view.*
import kotlinx.android.synthetic.main.fragment_mine_list_photo.view.*
import kotlinx.android.synthetic.main.fragment_mine_list_photo_area.view.*
import kotlinx.android.synthetic.main.fragment_mine_settings.view.*

/**
 */
class MineRecyclerViewAdapter(val fragment: Fragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val settings = ArrayList<Setting>()
    private val photos = ArrayList<String>()
    private var onSettingItemClickListener: OnSettingItemClickListener? = null
    private var onSignOutClickListener: OnSignOutClickListener? = null

    fun setOnSettingItemClickListener(onSettingItemClickListener: OnSettingItemClickListener) {
        this.onSettingItemClickListener = onSettingItemClickListener
    }

    fun setOnSignOutClickListener(onSignOutClickListener: OnSignOutClickListener) {
        this.onSignOutClickListener = onSignOutClickListener
    }

    var amountInfo: AmountInfo? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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

        val addr = "http://pic.58pic.com/58pic/15/35/50/50X58PICZkd_1024.jpg"

//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
//        photos.add(addr)
    }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 0 //head
            1 -> 1 //photos
            itemCount - 1 -> 3 //logout
            else -> 2 //setting
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> HeadViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_mine_list_header, parent, false))
            1 -> PhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_mine_list_photo_area, parent, false))
            2 -> SettingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_mine_settings, parent, false))
            else -> FooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_mine_footer, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeadViewHolder -> holder.setData(amountInfo)
            is SettingViewHolder -> holder.setData(setting = settings[position - 2])
            is PhotoViewHolder -> holder.setData(photos)
            is FooterViewHolder -> {

            }
        }
    }

    override fun getItemCount(): Int = settings.size + 3

    inner class HeadViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        private val context: Context = mView.context
        private val mRecentVisitor: TextView = mView.recentVisitor
        private val mRecentVisitorButton: LinearLayout = mView.recentVisitorButton
        private val mFollowMe: TextView = mView.followMe
        private val mFollowMeButton: LinearLayout = mView.followMeButton
        private val mMyDating: TextView = mView.myDating
        private val mMyDatingButton: LinearLayout = mView.myDatingButton
        private val mAuthenticate = mView.authenticate

        fun setData(amountInfo: AmountInfo?) {
            amountInfo?.let {
                mRecentVisitor.text = "${it.countVisitor}"
                mFollowMe.text = "${it.countFollowed}"
                mMyDating.text = "${it.countDating}"
            }
            mFollowMeButton.setOnClickListener { context.startActivity(CommonActivity.newIntent<UserActivity>(context, Page.VIEW_FOLLOW_ME)) }
            mRecentVisitorButton.setOnClickListener { context.startActivity(CommonActivity.newIntent<UserActivity>(context, Page.VIEW_RECENT_VISITOR)) }
            mMyDatingButton.setOnClickListener { context.startActivity(CommonActivity.newIntent<DatingActivity>(context, Page.VIEW_DATING_LIST)) }
            mAuthenticate.setOnClickListener { }

        }
    }

    inner class PhotoViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        private val context = mView.context
        private var noPhotoStubInflated = false
        private var hasPhotoStubInflated = false

        fun setData(photos: List<String>) {
            if (photos.isNotEmpty()) {
                if (!hasPhotoStubInflated) {
                    hasPhotoStubInflated = true
                    mView.hasPhotoStub.inflate()
                }
                val flexbox = mView.flexbox
                flexbox.viewAdapter = PhotoFlowAdapter(context).setUrls(photos).setMaxShow(12).setLastAsAdd(true)
                flexbox.setOnItemClickListener { view, index ->
                    Log.d("TTTT", "index=$index")
                }
            } else {
                Log.d("TTTT", "mView.noPhotoStub===>${mView.noPhotoStub}")
                if (!noPhotoStubInflated) {
                    noPhotoStubInflated = true
                    mView.noPhotoStub.inflate()
                }
                mView.uploadPhoto.setOnClickListener {
                    Log.d("TTTT", "vvvvvvvvvvvvvvvvvvvvv")
                }
            }
        }
    }

    inner class SettingViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        private val context: Context = mView.context
        private val mIcon: ImageView = mView.icon
        private val mTitle: TextView = mView.title
        private val mInfo: TextView = mView.info
        private val mNews: View = mView.news
        private lateinit var setting: Setting

        fun setData(setting: Setting) {
            this.setting = setting
            mView.setOnClickListener(this)
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
        }

        override fun onClick(v: View?) {
            onSettingItemClickListener?.onSettingItemClick(setting)
        }
    }

    inner class FooterViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        init {
            mView.signout.setOnClickListener {
                onSignOutClickListener?.onSignOutClick()
            }
        }
    }
}

interface OnSettingItemClickListener {
    fun onSettingItemClick(setting: Setting)
}

interface OnSignOutClickListener {
    fun onSignOutClick()
}


data class Setting(val icon: Int, val title: String, var info: String?)