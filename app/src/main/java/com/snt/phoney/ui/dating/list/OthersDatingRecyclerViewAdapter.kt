package com.snt.phoney.ui.dating.list


import android.os.Bundle
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
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.startActivity
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.photo.PhotoViewerActivity
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.widget.PhotoFlowAdapter
import kotlinx.android.synthetic.main.fragment_othersdating.view.*

/**
 */
class OthersDatingRecyclerViewAdapter(private val fragment: OthersDatingFragment, private val viewModel: DatingViewModel) : RecyclerView.Adapter<OthersDatingRecyclerViewAdapter.ViewHolder>() {

    var data: List<Dating>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_othersdating, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data!![position])
    }

    override fun getItemCount(): Int = data?.size ?: 0

    @Suppress("JoinDeclarationAndAssignment")
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        private val context = mView.context
        private val state = mView.state
        private val head = mView.head
        private val name = mView.name
        private val city = mView.city
        private val age = mView.age
        private val job = mView.job
        private val reliableValue = mView.reliableValue
        private val publishTime = mView.publishTime
        private val content = mView.content
        private val photos = mView.photos
        private val follow = mView.follow
        private val viewDetail = mView.viewDetail

        private val photoFlowAdapter: PhotoFlowAdapter
        private val onPhotoItemClickListener: (view: View, index: Int) -> Unit

        private var data: Dating? = null

        init {
            photoFlowAdapter = PhotoFlowAdapter(mView.context).setLastAsAdd(false)
            onPhotoItemClickListener = { _, index ->
                data?.let { data ->
                    fragment.startActivity<PhotoViewerActivity>(Page.PHOTO_VIEWER, Bundle().apply {
                        putStringArrayList(Constants.Extra.URL_LIST, ArrayList(data.photoUrls()))
                        putInt(Constants.Extra.INDEX, index)
                        putBoolean(Constants.Extra.DELETABLE, false)
                    })
                }
            }
        }

        fun setData(dating: Dating) {
            this.data = dating
            if (dating.state == 0 || dating.state == 1) {
                state.setTextColor(context.colorOf(R.color.colorPrimaryMale))
                state.setText(R.string.on_going)
            } else if (dating.state == 2) {
                state.setTextColor(context.colorOf(R.color.dark_gray))
                state.setText(R.string.has_out_of_time)
            } else if (dating.state == 3) {
                state.setTextColor(context.colorOf(R.color.dark_gray))
                state.setText(R.string.has_finish)
            } else if (dating.state == 5) {
                state.setTextColor(context.colorOf(R.color.dark_gray))
                state.setText(R.string.has_canceled)
            }
            dating.user?.let { user ->
                user?.avatar?.let { portrait ->
                    Glide.with(mView).load(portrait).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(head)
                }
                name.text = user.nickname
                city.text = user.city
                age.text = user.age.toString()
                job.text = user.career

                follow.setOnClickListener {
                    viewModel.follow(dating)
                }
            }

            if (dating.isCared) {
                val cd = context.getDrawable(R.drawable.ic_heart_solid_red)
                val size = context.dip(12)
                cd.setBounds(0, 0, size, size)
                follow.setCompoundDrawables(cd, null, null, null)
            } else {
                val cd = context.getDrawable(R.drawable.ic_heart_border)
                val size = context.dip(12)
                cd.setBounds(0, 0, size, size)
                follow.setCompoundDrawables(cd, null, null, null)
            }

            content.text = dating.content

            photoFlowAdapter.setUrls(dating.photoUrls())
            photos.setOnItemClickListener(onPhotoItemClickListener)
            photos.viewAdapter = photoFlowAdapter

            publishTime.text = context.getString(R.string.publish_time_template, dating.formatCreateTime())

            viewDetail.setOnClickListener {
                fragment.startActivity<DatingActivity>(Page.DATING_DETAIL, Bundle().apply {
                    putString(Constants.Extra.UUID, dating.uuid)
                })
            }
        }


    }
}
