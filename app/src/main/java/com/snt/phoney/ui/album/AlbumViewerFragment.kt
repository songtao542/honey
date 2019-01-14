package com.snt.phoney.ui.album

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.di.Injectable
import com.snt.phoney.domain.model.Photo
import com.snt.phoney.ui.photo.PhotoFragment
import com.snt.phoney.ui.photo.PhotoViewerFragment
import javax.inject.Inject

class AlbumViewerFragment : PhotoViewerFragment(), Injectable {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = AlbumViewerFragment().apply {
            this.arguments = arguments
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AlbumViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AlbumViewModel::class.java)

        setOnPhotoDeleteListener { _, photo ->
            viewModel.deletePhotos(ArrayList<Photo>().apply { add(photo) })
        }
    }

    override fun newPhotoViewFragment(photo: Photo): PhotoFragment {
        return AlbumPhotoFragment.newInstance(photo)
    }

}