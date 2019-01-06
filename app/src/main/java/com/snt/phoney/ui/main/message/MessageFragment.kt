package com.snt.phoney.ui.main.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.Page
import com.snt.phoney.di.Injectable
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.enableOptionsMenu
import com.snt.phoney.extensions.startActivity
import com.snt.phoney.ui.album.AlbumActivity
import com.snt.phoney.ui.browser.BrowserActivity
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.nearby.NearbyActivity
import jiguang.chat.fragment.ConversationListFragment
import kotlinx.android.synthetic.main.fragment_conv_list.*
import kotlinx.android.synthetic.main.fragment_message.view.*
import javax.inject.Inject

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 */
class MessageFragment : ConversationListFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MessageViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enableOptionsMenu(toolbar, false, R.menu.message)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MessageViewModel::class.java)

        val inflater = LayoutInflater.from(requireContext())
        val officialMessageView = inflater.inflate(R.layout.fragment_message, null)
        officialMessageView.head.setImageResource(R.drawable.ic_official)
        officialMessageView.title.text = getString(R.string.chunmi_official)
        officialMessageView.subTitle.text = getString(R.string.chunmi_official_no_message)
        officialMessageView.setOnClickListener {
            startActivity<BrowserActivity>(Page.OFFICIAL_MESSAGE)
        }
        listView.addHeaderView(officialMessageView)

        val datingApplying = inflater.inflate(R.layout.fragment_message, null)
        datingApplying.head.setImageResource(R.drawable.ic_applying)
        datingApplying.title.text = getString(R.string.dating_applying)
        datingApplying.subTitle.text = getString(R.string.chunmi_official_no_message)
        datingApplying.setOnClickListener {
            startActivity<DatingActivity>(Page.DATING_APPLICANT_LIST)
        }
        listView.addHeaderView(datingApplying)

        val photoApplying = inflater.inflate(R.layout.fragment_message, null)
        photoApplying.head.setImageResource(R.drawable.ic_applying)
        photoApplying.title.text = getString(R.string.photo_applying)
        photoApplying.subTitle.text = getString(R.string.chunmi_official_no_message)
        photoApplying.setOnClickListener {
            startActivity<AlbumActivity>(Page.PHOTO_APPLY_LIST)
        }
        listView.addHeaderView(photoApplying)

        viewModel.messages.observe(this, Observer {
            swipeRefresh.isRefreshing = false
            it?.let { messages ->
                if (messages.isNotEmpty()) {
                    val message = messages[0]
                    officialMessageView.subTitle.text = message.title
                }
            }
        })

        viewModel.applicants.observe(this, Observer {
            swipeRefresh.isRefreshing = false
            it?.let { applicants ->
                if (applicants.isNotEmpty()) {
                    val applicant = applicants[0]
                    datingApplying.subTitle.text = getString(R.string.user_apply_your_dating_template, applicant.user?.nickname)
                }
            }
        })

        viewModel.photoApplyList.observe(this, Observer {
            swipeRefresh.isRefreshing = false
            it?.let { photoApplyList ->
                if (photoApplyList.isNotEmpty()) {
                    val apply = photoApplyList[0]
                    photoApplying.subTitle.text = getString(R.string.user_apply_your_photo_template, apply.nickname)
                }
            }
        })
        swipeRefresh.setProgressViewOffset(false, -dip(40), dip(8))
        swipeRefresh.setSlingshotDistance(dip(64))
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            load()
        }

        load()
    }

    private fun load() {
        viewModel.listOfficialMessage()
        viewModel.listApplicant()
        viewModel.listPhotoApply()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.message, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.friendRecommend -> {
                startActivity<NearbyActivity>(Page.NEARBY)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MessageFragment()
    }
}
