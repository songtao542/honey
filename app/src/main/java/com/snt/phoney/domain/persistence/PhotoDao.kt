package com.snt.phoney.domain.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.snt.phoney.domain.model.Photo
import io.reactivex.Single

@Dao
abstract class PhotoDao {
    @Query("SELECT * FROM PHOTO")
    internal abstract fun getPhotos(): List<Photo>?

    @Query("SELECT * FROM PHOTO WHERE viewerId=:viewerId")
    internal abstract fun getPhotosByViewer(viewerId: String): List<Photo>?

    @Query("DELETE FROM PHOTO")
    internal abstract fun deleteAll()

    @Query("DELETE FROM PHOTO WHERE id=:id")
    internal abstract fun delete(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract fun insertPhoto(photo: Photo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract fun insertPhotos(photos: List<Photo>)


    fun getPhotosByViewerAsync(viewerId: String): Single<List<Photo>> {
        return Single.create<List<Photo>> {
            try {
                val result = getPhotosByViewer(viewerId) ?: emptyList()
                it.onSuccess(result)
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }
}