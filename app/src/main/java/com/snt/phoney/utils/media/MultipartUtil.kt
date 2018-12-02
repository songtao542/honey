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
            val requestBody = RequestBody.create(MediaType.parse(mediaType.mimeType), file)
            val part = MultipartBody.Part.createFormData(name, file.name, requestBody)
            parts.add(part)
        }
        return parts
    }


}