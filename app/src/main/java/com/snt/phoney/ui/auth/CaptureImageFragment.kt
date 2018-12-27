package com.snt.phoney.ui.auth

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.cameraview.AspectRatio
import com.google.android.cameraview.CameraView
import com.snt.phoney.R
import kotlinx.android.synthetic.main.fragment_capture_image.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


/**
 * This demo app saves the taken picture to a constant file.
 * $ adb pull /sdcard/Android/data/com.google.android.cameraview.demo/files/Pictures/picture.jpg
 */
class CaptureImageFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {

    companion object {

        fun newInstance(): CaptureImageFragment {
            return CaptureImageFragment()
        }

        private const val TAG = "CaptureImageFragment"

        private const val REQUEST_CAMERA_PERMISSION = 1

        private val FLASH_OPTIONS = intArrayOf(CameraView.FLASH_AUTO, CameraView.FLASH_OFF, CameraView.FLASH_ON)

        private val FLASH_ICONS = intArrayOf(R.drawable.ic_capture_flash_auto, R.drawable.ic_capture_flash_off, R.drawable.ic_capture_flash_on)

        private val FLASH_TITLES = intArrayOf(R.string.flash_auto, R.string.flash_off, R.string.flash_on)
    }

    private var mCurrentFlash: Int = 0

    private val handler = Handler()

    private var imageSavePath: String? = null

    private val backgroundHandler: Handler by lazy {
        val thread = HandlerThread("background")
        thread.start()
        return@lazy Handler(thread.looper)
    }


    private val callback = object : CameraView.Callback() {

        override fun onCameraOpened(cameraView: CameraView) {
            Log.d(TAG, "onCameraOpened")
        }

        override fun onCameraClosed(cameraView: CameraView) {
            Log.d(TAG, "onCameraClosed")
        }

        override fun onPictureTaken(cameraView: CameraView, data: ByteArray) {
            Log.d(TAG, "onPictureTaken " + data.size)
            backgroundHandler.post {
                imageSavePath = getImageFilePath(requireContext())
                val file = File(imageSavePath)
                var os: OutputStream? = null
                try {
                    os = FileOutputStream(file)
                    os.write(data)
                    os.close()
                } catch (e: IOException) {
                    Log.w(TAG, "Cannot write to $file", e)
                } finally {
                    if (os != null) {
                        try {
                            os.close()
                        } catch (e: IOException) {
                            Log.e(TAG, "error:" + e.message)
                        }

                    }
                    handler.post { finish() }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_capture_image, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        cameraView.addCallback(callback)
        cameraView.setAspectRatio(AspectRatio.of(4, 3))
        cameraView.facing = CameraView.FACING_FRONT

        takePicture.setOnClickListener {
            cameraView.takePicture()
        }
        activity?.let { activity ->
            if (activity is AppCompatActivity) {
                activity?.setSupportActionBar(toolbar)
                activity?.supportActionBar?.setDisplayShowTitleEnabled(false)
            }
        }
    }

    @Suppress("CascadeIf")
    override fun onResume() {
        super.onResume()
        if (checkPermission()) {
            cameraView.start()
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (checkPermission()) {
                    cameraView.start()
                } else {
                    if (activity != null) {
                        activity?.onBackPressed()
                    }
                }
            }
        }
    }

    override fun onPause() {
        cameraView?.stop()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (backgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                backgroundHandler.looper.quitSafely()
            } else {
                backgroundHandler.looper.quit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.capture_image, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.switch_flash -> {
                if (cameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.size
                    item.setTitle(FLASH_TITLES[mCurrentFlash])
                    item.setIcon(FLASH_ICONS[mCurrentFlash])
                    cameraView?.flash = FLASH_OPTIONS[mCurrentFlash]
                }
                return true
            }
            R.id.switch_camera -> {
                if (cameraView != null) {
                    val facing = cameraView.facing
                    cameraView?.facing = if (facing == CameraView.FACING_FRONT) CameraView.FACING_BACK else CameraView.FACING_FRONT
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun finish() {
        onResultListener?.invoke(imageSavePath)
        activity?.onBackPressed()
    }

    private var onResultListener: ((path: String?) -> Unit)? = null

    fun setOnResultListener(listener: ((path: String?) -> Unit)) {
        onResultListener = listener
    }

    private fun getImageFilePath(context: Context): String {
        val filename = System.currentTimeMillis().toString() + ".jpg"
        val dir = context.getExternalFilesDir(null)

        return if (dir == null) {
            filename
        } else {
            dir.absolutePath + filename
        }
    }


}
