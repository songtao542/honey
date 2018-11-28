package com.snt.phoney.ui.main.square.official


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snt.phoney.R
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.ui.main.square.SquareViewModel
import com.snt.phoney.widget.PhotoFlowAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_official_recommend.view.*

/**
 */
class OfficialRecommendRecyclerViewAdapter(val viewModel: SquareViewModel, val disposeBag: CompositeDisposable) : RecyclerView.Adapter<OfficialRecommendRecyclerViewAdapter.ViewHolder>() {


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
        fun setData(data: Dating?) {
            if (data == null) {
                return
            }
            Glide.with(mView).load(data.user?.portrait).into(mView.head)
            mView.flowImages.viewAdapter = PhotoFlowAdapter(mView.context).setUrls(data.photoUrls()).setLastAsAdd(false)
            mView.name.text = data.user?.nickname
            mView.age.text = mView.context.getString(R.string.age_value_template, data.user?.age)
            mView.publishTime.text = getPublishTime(data.createTime)
            mView.content.text = data.content

            mView.attend.setOnClickListener {
                data.uuid?.let { uuid ->
                    viewModel.joinDating(uuid)
                            ?.subscribeBy { response ->
                                if (response.code == 200) {
                                    mView.attend.text = getString(R.string.joined_dating)
                                }
                            }
                            ?.disposedBy(disposeBag)
                }
            }
            mView.follow.text = if (data.care) getString(R.string.follow) else getString(R.string.followed)
            mView.follow.setOnClickListener {
                data.user?.uuid?.let { uuid ->
                    viewModel.follow(uuid)
                            ?.subscribeBy { response ->
                                mView.follow.text = if (response.data == true) getString(R.string.follow) else getString(R.string.followed)
                            }
                            ?.disposedBy(disposeBag)
                }
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
