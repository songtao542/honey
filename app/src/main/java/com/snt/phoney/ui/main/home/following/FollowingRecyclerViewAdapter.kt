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
import com.snt.phoney.utils.Chat
import io.reactivex.disposables.CompositeDisposable
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
        private val context = mView.context
        private val chat = mView.chat
        private val follow = mView.follow
        private val name = mView.name
        private val content = mView.content
        private val head = mView.head

        fun setData(user: User?) {
            if (user == null) {
                return
            }
            //Glide.with(mView).load(data.avatar).into(mView.head)
            Glide.with(mView).load(user.avatar).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(head)

            name.text = user.nickname
            content.text = user.introduce
            chat.setOnClickListener {
                user.im?.let { im ->
                    Chat.start(context, im)
                }
            }
            follow.setOnClickListener {
                viewModel.follow(user)
            }
        }
    }
}
