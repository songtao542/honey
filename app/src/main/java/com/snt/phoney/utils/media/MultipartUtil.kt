package com.snt.phoney.utils.media

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object MultipartUtil {

    fun getMultipartList(name: String, files: List<File>): List<MultipartBody.Part> {
        val parts = ArrayList<MultipartBody.Part>(files.size)
        for (file in files) {
            val mediaType = MediaFile.getFileType(file.path)
            val mimeType = when {
                mediaType != null -> mediaType.mimeType
                file.path.contains("jpeg", true) -> "image/jpeg"
                file.path.contains("png", true) -> "image/png"
                file.path.contains("gif", true) -> "image/gif"
                else -> "image/jpeg"
            }
            val requestBody = RequestBody.create(MediaType.parse(mimeType), file)
            val part = MultipartBody.Part.createFormData(name, file.name, requestBody)
            parts.add(part)
        }
        return parts
    }


}