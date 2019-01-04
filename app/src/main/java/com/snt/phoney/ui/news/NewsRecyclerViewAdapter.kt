package com.snt.phoney.ui.news


import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.domain.model.News
import com.snt.phoney.ui.browser.WebBrowserActivity
import com.snt.phoney.utils.TimeFormat
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_news_list_item.view.*

/**
 */
class NewsRecyclerViewAdapter(private val fragment: NewsFragment) : RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>() {

    var data: List<News>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_news_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data!![position])
    }

    override fun getItemCount(): Int = data?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        private val context = mView.context
        private val newsTitle = mView.newsTitle
        private val image = mView.image
        private val publishTime = mView.publishTime

        fun setData(news: News) {
            Glide.with(mView)
                    .applyDefaultRequestOptions(RequestOptions()
                            .placeholder(R.drawable.ic_placeholder))
                    .load(news.cover).into(image)
            newsTitle.text = news.title
            val createTime = TimeFormat.format(context, news.createTime)
            publishTime.text = context.getString(R.string.publish_time_template, if (TextUtils.isEmpty(createTime)) news.formatCreateTime() else createTime)

            mView.setOnClickListener {
                context.startActivity(Intent(context, WebBrowserActivity::class.java).apply {
                    putExtra(Constants.Extra.TITLE, news.title)
                    putExtra(Constants.Extra.URL, news.url)
                })
            }
        }

    }
}
