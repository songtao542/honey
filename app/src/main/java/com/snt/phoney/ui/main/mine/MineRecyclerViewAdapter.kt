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
import com.snt.phoney.domain.model.Photo
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
    var photos: List<Photo>? = null
    private var onSettingItemClickListener: OnSettingItemClickListener? = null
    private var onSignOutClickListener: OnSignOutClickListener? = null
    private var onAddPhotoClickListener: OnAddPhotoClickListener? = null
    private var onPhotoClickListener: OnPhotoClickListener? = null

    fun setOnSettingItemClickListener(onSettingItemClickListener: OnSettingItemClickListener) {
        this.onSettingItemClickListener = onSettingItemClickListener
    }

    fun setOnSignOutClickListener(onSignOutClickListener: OnSignOutClickListener) {
        this.onSignOutClickListener = onSignOutClickListener
    }

    fun setOnAddPhotoClickListener(onAddPhotoClickListener: OnAddPhotoClickListener) {
        this.onAddPhotoClickListener = onAddPhotoClickListener
    }

    fun setOnPhotoClickListener(onPhotoClickListener: OnPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener
    }

    var amountInfo: AmountInfo? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        settings.add(Setting(R.drawable.ic_photo_permission, "相册权限", ""))
        settings.add(Setting(R.drawable.ic_my_dating, "我的约会", "新消息"))
        settings.add(Setting(R.drawable.ic_my_wallet, "我的钱包", ""))
        settings.add(Setting(R.drawable.ic_privacy_setting, "隐私设置", ""))
        settings.add(Setting(R.drawable.ic_bind_phone, "绑定手机", ""))
        settings.add(Setting(R.drawable.ic_share, "分享给好友", ""))
        settings.add(Setting(R.drawable.ic_user_protocol, "用户协议", ""))
        settings.add(Setting(R.drawable.ic_clear_cache, "清理缓存", ""))
        settings.add(Setting(R.drawable.ic_about, "关于", ""))
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
            mMyDatingButton.setOnClickListener { context.startActivity(CommonActivity.newIntent<DatingActivity>(context, Page.VIEW_MY_DATING)) }
            mAuthenticate.setOnClickListener { }

        }
    }

    inner class PhotoViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        private val context = mView.context
        private var noPhotoStubInflated = false
        private var hasPhotoStubInflated = false
        private var photos: List<Photo>? = null

        fun setData(photoList: List<Photo>?) {
            if (photoList != null && photoList.isNotEmpty()) {
                photos = photoList
                photos?.let { photos ->
                    if (!hasPhotoStubInflated) {
                        hasPhotoStubInflated = true
                        mView.hasPhotoStub.inflate()
                    } else {
                        mView.photosLayout.visibility = View.VISIBLE
                    }
                    if (noPhotoStubInflated) {
                        mView.noPhotosLayout.visibility = View.GONE
                    }
                    val flexbox = mView.flexbox
                    flexbox.viewAdapter = PhotoFlowAdapter(context).setUrls(photos.map { it.path!! }).setMaxShow(19).setLastAsAdd(true).setOnAddClickListener {
                        onAddPhotoClickListener?.onAddPhotoClick()
                    }
                    flexbox.setOnItemClickListener { _, index ->
                        onPhotoClickListener?.onPhotoClick(index, photos[index])
                    }
                }
            } else {
                Log.d("TTTT", "mView.noPhotoStub===>${mView.noPhotoStub}")
                if (!noPhotoStubInflated) {
                    noPhotoStubInflated = true
                    mView.noPhotoStub.inflate()
                } else {
                    mView.noPhotosLayout.visibility = View.VISIBLE
                }
                if (hasPhotoStubInflated) {
                    mView.photosLayout.visibility = View.GONE
                }
                mView.uploadPhoto.setOnClickListener {
                    onAddPhotoClickListener?.onAddPhotoClick()
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

interface OnPhotoClickListener {
    fun onPhotoClick(index: Int, photo: Photo)
}

interface OnAddPhotoClickListener {
    fun onAddPhotoClick()
}

interface OnSettingItemClickListener {
    fun onSettingItemClick(setting: Setting)
}

interface OnSignOutClickListener {
    fun onSignOutClick()
}


data class Setting(val icon: Int, val title: String, var info: String?)