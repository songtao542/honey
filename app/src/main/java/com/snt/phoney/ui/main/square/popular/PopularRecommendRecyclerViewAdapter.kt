package com.snt.phoney.ui.main.square.popular


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.main.square.SquareViewModel
import com.snt.phoney.ui.photo.PhotoViewerActivity
import com.snt.phoney.ui.user.UserActivity
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.widget.PhotoFlowAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_official_recommend.view.*

/**
 */
class PopularRecommendRecyclerViewAdapter(val fragment: Fragment, val viewModel: SquareViewModel, val disposeBag: CompositeDisposable) : RecyclerView.Adapter<PopularRecommendRecyclerViewAdapter.ViewHolder>() {

    var data: List<Dating>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_popular_recommend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data!![position])
    }

    override fun getItemCount(): Int = data?.size ?: 0

    @Suppress("JoinDeclarationAndAssignment")
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        private val photoFlowAdapter: PhotoFlowAdapter
        private val onPhotoItemClickListener: (view: View, index: Int) -> Unit
        private var data: Dating? = null

        private val flowImagesView = mView.flowImages
        private val headView = mView.head
        private val nameView = mView.name
        private val ageView = mView.age
        private val publishTimeView = mView.publishTime
        private val contentView = mView.content
        private val attendView = mView.attend
        private val followView = mView.follow

        init {
            photoFlowAdapter = PhotoFlowAdapter(mView.context).setLastAsAdd(false)
            onPhotoItemClickListener = { _, index ->
                data?.let { data ->
                    fragment.startActivity(CommonActivity.newIntent<PhotoViewerActivity>(fragment.requireContext(), Page.PHOTO_VIEWER, Bundle().apply {
                        putStringArrayList(Constants.Extra.URL_LIST, ArrayList(data.photoUrls()))
                        putInt(Constants.Extra.INDEX, index)
                        putBoolean(Constants.Extra.DELETABLE, false)
                    }))
                }
            }
        }

        fun setData(data: Dating?) {
            if (data == null) {
                return
            }
            this.data = data
            //Glide.with(mView).load(data.user?.portrait).into(mView.head)
            Glide.with(mView).load(data.user?.portrait).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(headView)
            photoFlowAdapter.setUrls(data.photoUrls())
            flowImagesView.setOnItemClickListener(onPhotoItemClickListener)
            flowImagesView.viewAdapter = photoFlowAdapter

            nameView.text = data.user?.nickname
            ageView.text = mView.context.getString(R.string.age_value_template, data.user?.age)
            publishTimeView.text = getPublishTime(data.createTime)
            contentView.text = data.content

            attendView.setOnClickListener {
                data.uuid?.let { uuid ->
                    viewModel.joinDating(uuid)
                            ?.subscribeBy { response ->
                                if (response.code == 200) {
                                    attendView.text = getString(R.string.joined_dating)
                                }
                            }
                            ?.disposedBy(disposeBag)
                }
            }
            followView.text = if (data.care) getString(R.string.follow) else getString(R.string.followed)
            followView.setOnClickListener {
                data.user?.uuid?.let { uuid ->
                    viewModel.follow(uuid)
                            ?.subscribeBy { response ->
                                followView.text = if (response.data == true) getString(R.string.follow) else getString(R.string.followed)
                            }
                            ?.disposedBy(disposeBag)
                }
            }

            headView.setOnClickListener {
                mView.context.startActivity(CommonActivity.newIntent<UserActivity>(mView.context, Page.VIEW_USER_INFO, Bundle().apply {
                    putParcelable(Constants.Extra.USER, data.user)
                }))
            }

            contentView.setOnClickListener {
                mView.context.startActivity(CommonActivity.newIntent<DatingActivity>(mView.context, Page.VIEW_DATING_DETAIL, Bundle().apply {
                    putString(Constants.Extra.UUID, data.uuid)
                }))
            }
        }


        private fun getPublishTime(time: Long): String {
            return "刚刚"
        }


        private fun getString(resId: Int): String {
            return mView.context.getString(resId)
        }
    }
}
