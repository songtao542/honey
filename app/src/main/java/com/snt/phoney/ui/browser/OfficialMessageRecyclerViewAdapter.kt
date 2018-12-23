package com.snt.phoney.ui.browser


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snt.phoney.R
import com.snt.phoney.domain.model.OfficialMessage
import com.snt.phoney.utils.data.Constants
import jiguang.chat.utils.TimeFormat
import kotlinx.android.synthetic.main.fragment_officialmessage.view.*

/**
 */
class OfficialMessageRecyclerViewAdapter() : RecyclerView.Adapter<OfficialMessageRecyclerViewAdapter.ViewHolder>() {


    var data: List<OfficialMessage>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_officialmessage, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data!![position])
    }

    override fun getItemCount(): Int = data?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        private val context = mView.context
        private val createTime = mView.createTime
        private val imageView = mView.imageView
        private val title = mView.title
        private val subTitle = mView.subTitle

        fun setData(officialMessage: OfficialMessage) {
            createTime.text = TimeFormat(context, officialMessage.crateTime).time
            Glide.with(mView).load(officialMessage.cover).into(imageView)
            title.text = officialMessage.title
            subTitle.text = officialMessage.subTitle

            mView.setOnClickListener {
                context.startActivity(Intent(context, WebBrowserActivity::class.java).apply {
                    putExtra(Constants.Extra.TITLE, officialMessage.title)
                    putExtra(Constants.Extra.URL, officialMessage.url)
                })
            }
        }
    }
}
