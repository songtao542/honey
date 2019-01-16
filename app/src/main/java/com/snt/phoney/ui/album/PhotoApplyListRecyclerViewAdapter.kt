package com.snt.phoney.ui.album


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.domain.model.PhotoApply
import com.snt.phoney.domain.model.Sex
import kotlinx.android.synthetic.main.fragment_album_photo_apply_list_item.view.*
import java.text.DecimalFormat

/**
 */
class PhotoApplyListRecyclerViewAdapter(private val viewModel: AlbumViewModel) : RecyclerView.Adapter<PhotoApplyListRecyclerViewAdapter.ViewHolder>() {

    var data: List<PhotoApply>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_album_photo_apply_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data!![position])
    }

    override fun getItemCount(): Int = data?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val context: Context = mView.context

        private val head = mView.head
        private val name = mView.name
        private val content = mView.content
        private val stateText = mView.stateText
        private val agreeButton = mView.agreeButton
        private val rejectButton = mView.rejectButton
        private val createTime = mView.createTime

        fun setData(photoApply: PhotoApply) {
            photoApply.avatar?.let { portrait ->
                Glide.with(mView).load(portrait).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(head)
            }
            name.text = photoApply.nickname
            val df = DecimalFormat.getInstance()
            if (photoApply.sex == Sex.MALE.value) {
                content.text = context.getString(R.string.dating_applying_content_m_template,
                        df.format(photoApply.height),
                        df.format(photoApply.age),
                        df.format(photoApply.weight))
            } else {
                content.text = context.getString(R.string.dating_applying_content_f_template,
                        df.format(photoApply.height),
                        df.format(photoApply.age),
                        photoApply.cup)
            }
            createTime.text = photoApply.formatCreateTime()
            if (photoApply.state == 0) {
                stateText.visibility = View.GONE
                agreeButton.visibility = View.VISIBLE
                rejectButton.visibility = View.VISIBLE
                agreeButton.setOnClickListener {
                    viewModel.reviewPhotoApply(photoApply, 0)
                }
                rejectButton.setOnClickListener {
                    viewModel.reviewPhotoApply(photoApply, 1)
                }
            } else {
                stateText.visibility = View.VISIBLE
                agreeButton.visibility = View.GONE
                rejectButton.visibility = View.GONE
                when (photoApply.state) {
                    1 -> stateText.text = context.getString(R.string.has_agree_h_view)
                    2 -> stateText.text = context.getString(R.string.has_reject_h_view)
                    10 -> stateText.text = context.getString(R.string.has_invalid)
                }
            }
        }
    }
}
