package com.snt.phoney.ui.main.home.following


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.disposedBy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_following.view.*

/**
 */
class FollowingRecyclerViewAdapter(private val viewModel: FollowingViewModel, val disposeBag: CompositeDisposable) : RecyclerView.Adapter<FollowingRecyclerViewAdapter.ViewHolder>() {

    var data: List<User>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_following, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data!![position])
    }

    override fun getItemCount(): Int = data?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        fun setData(data: User?) {
            if (data == null) {
                return
            }
            //Glide.with(mView).load(data.portrait).into(mView.head)
            Glide.with(mView).load(data.portrait).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(mView.head)

            mView.name.text = data.nickname
            mView.content.text = data.introduce
            mView.chat.setOnClickListener {

            }
            mView.follow.setOnClickListener {
                data.uuid?.let { uuid ->
                    viewModel.follow(uuid)
                            ?.subscribeBy {

                            }
                            ?.disposedBy(disposeBag)
                }

            }
        }
    }
}
