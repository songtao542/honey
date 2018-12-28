package com.snt.phoney.ui.nearby


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
import com.snt.phoney.utils.DistanceFormat
import kotlinx.android.synthetic.main.fragment_nearby.view.*

/**
 */
class NearbyRecyclerViewAdapter() : RecyclerView.Adapter<NearbyRecyclerViewAdapter.ViewHolder>() {

    var data: List<User>? = null
        set(value) {
            value?.let {
                field = it
                this@NearbyRecyclerViewAdapter.notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_nearby, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data!![position])
    }

    override fun getItemCount(): Int = data?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        private val context = mView.context
        private val head = mView.head
        private val name = mView.name
        private val distance = mView.distance
        private val chatWith = mView.chatWith

        fun setData(user: User) {
            Glide.with(mView).load(user.avatar).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(head)
            name.text = user.nickname
            distance.text = DistanceFormat.format(context, user.distance)
            chatWith.setOnClickListener {
                user.im?.let { im ->
                    Chat.start(context, im)
                }
            }
        }

    }
}
