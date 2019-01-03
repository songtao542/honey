package com.snt.phoney.ui.main.square.official


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.startActivity
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.main.square.SquareViewModel
import com.snt.phoney.ui.photo.PhotoViewerActivity
import com.snt.phoney.ui.user.UserActivity
import com.snt.phoney.utils.TimeFormat
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.widget.PhotoFlowAdapter
import kotlinx.android.synthetic.main.fragment_official_recommend.view.*

/**
 */
class OfficialRecommendRecyclerViewAdapter(val fragment: OfficialRecommendFragment, val viewModel: SquareViewModel) : RecyclerView.Adapter<OfficialRecommendRecyclerViewAdapter.ViewHolder>() {


    var data: List<Dating>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_official_recommend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data!![position])
    }

    override fun getItemCount(): Int = data?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        private val context = mView.context
        private val headView = mView.head
        private val nameView = mView.name
        private val reliableValueView = mView.reliableValue
        private val cityView = mView.city
        private val ageView = mView.age
        private val jobView = mView.job
        private val publishTimeView = mView.publishTime
        private val datingAddressView = mView.datingAddress
        private val datingTimeView = mView.datingTime
        private val contentView = mView.content
        private val attendView = mView.attend
        private val followView = mView.follow
        private val flowImagesView = mView.flowImages

        @Suppress("JoinDeclarationAndAssignment")
        private val photoFlowAdapter: PhotoFlowAdapter
        private val onPhotoItemClickListener: (view: View, index: Int) -> Unit

        private var dating: Dating? = null

        init {
            photoFlowAdapter = PhotoFlowAdapter(mView.context).setLastAsAdd(false)
            onPhotoItemClickListener = { _, index ->
                dating?.let { dating ->
                    fragment.startActivity<PhotoViewerActivity>(Page.PHOTO_VIEWER, Bundle().apply {
                        putStringArrayList(Constants.Extra.URL_LIST, ArrayList(dating.photoUrls()))
                        putInt(Constants.Extra.INDEX, index)
                        putBoolean(Constants.Extra.DELETABLE, false)
                    })
                }
            }
        }

        fun setData(data: Dating?) {
            data?.let { data ->
                dating = data
                Glide.with(mView).load(data.user?.avatar).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(headView)
                flowImagesView.viewAdapter = PhotoFlowAdapter(mView.context).setUrls(data.photoUrls()).setLastAsAdd(false)

                nameView.text = data.user?.nickname
                cityView.text = data.user?.city
                ageView.text = mView.context.getString(R.string.age_value_template, data.user?.age)
                jobView.text = data.user?.career
                val createTime = TimeFormat.format(context, data.createTime)
                publishTimeView.text = context.getString(R.string.publish_time_template, if (TextUtils.isEmpty(createTime)) data.formatCreateTime() else createTime)
                datingAddressView.text = data.city
                datingTimeView.text = data.formatTime()
                contentView.text = data.content

                photoFlowAdapter.setUrls(data.photoUrls())
                flowImagesView.setOnItemClickListener(onPhotoItemClickListener)
                flowImagesView.viewAdapter = photoFlowAdapter

                if (!data.isAttend) {
                    attendView.setText(R.string.join_dating)
                    attendView.setOnClickListener {
                        viewModel.joinDating(data, true)
                    }
                } else {
                    attendView.setText(R.string.joined_dating)
                }
                setCareState(data.isCared)
                followView.setOnClickListener {
                    viewModel.follow(data, true)
                }

                headView.setOnClickListener {
                    context.startActivity<UserActivity>(Page.USER_INFO, Bundle().apply {
                        putParcelable(Constants.Extra.USER, data.user)
                    })
                }

                mView.setOnClickListener {
                    context.startActivity<DatingActivity>(Page.DATING_DETAIL, Bundle().apply {
                        putString(Constants.Extra.UUID, data.uuid)
                    })
                }
            }
        }

        private fun setCareState(isCared: Boolean) {
            if (isCared) {
                val cd = context.getDrawable(R.drawable.ic_heart_solid_red)
                val size = context.dip(12)
                cd.setBounds(0, 0, size, size)
                followView.setCompoundDrawables(cd, null, null, null)
            } else {
                val cd = context.getDrawable(R.drawable.ic_heart_border)
                val size = context.dip(12)
                cd.setBounds(0, 0, size, size)
                followView.setCompoundDrawables(cd, null, null, null)
            }
        }
    }
}
