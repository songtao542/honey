package com.snt.phoney.ui.main.square.popular


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snt.phoney.R
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.startActivity
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.main.square.SquareViewModel
import com.snt.phoney.ui.photo.PhotoViewerActivity
import com.snt.phoney.ui.user.UserActivity
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.widget.PhotoFlowAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import jiguang.chat.utils.TimeFormat
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
                Glide.with(mView).load(data.user?.avatar).into(headView)
                flowImagesView.viewAdapter = PhotoFlowAdapter(mView.context).setUrls(data.photoUrls()).setLastAsAdd(false)

                nameView.text = data.user?.nickname
                cityView.text = data.user?.city
                ageView.text = mView.context.getString(R.string.age_value_template, data.user?.age)
                jobView.text = data.user?.career
                publishTimeView.text = TimeFormat(context, data.createTime).detailTime
                datingAddressView.text = data.city
                datingTimeView.text = data.formatTime()
                contentView.text = data.content

                photoFlowAdapter.setUrls(data.photoUrls())
                flowImagesView.setOnItemClickListener(onPhotoItemClickListener)
                flowImagesView.viewAdapter = photoFlowAdapter

                attendView.setOnClickListener {
                    dating?.uuid?.let { uuid ->
                        viewModel.joinDating(uuid)
                                ?.subscribeBy(
                                        onSuccess = { response ->
                                            if (response.code == 200) {
                                                attendView.text = context.getString(R.string.joined_dating)
                                            }
                                        },
                                        onError = {

                                        })?.disposedBy(disposeBag)
                    }
                }
                followView.text = if (data.isCared) context.getString(R.string.follow) else context.getString(R.string.followed)
                followView.setOnClickListener {
                    dating?.user?.uuid?.let { uuid ->
                        viewModel.follow(uuid)
                                ?.subscribeBy(
                                        onSuccess = { response ->
                                            followView.text = if (response.data == true) context.getString(R.string.follow) else context.getString(R.string.followed)
                                        },
                                        onError = {

                                        })?.disposedBy(disposeBag)
                    }
                }

                headView.setOnClickListener {
                    context.startActivity<UserActivity>(Page.USER_INFO, Bundle().apply {
                        putParcelable(Constants.Extra.USER, data.user)
                    })
                }

                contentView.setOnClickListener {
                    context.startActivity<DatingActivity>(Page.DATING_DETAIL, Bundle().apply {
                        putString(Constants.Extra.UUID, data.uuid)
                    })
                }
            }
        }
    }
}
