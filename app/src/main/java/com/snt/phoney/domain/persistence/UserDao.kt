package com.snt.phoney.domain.persistence

import android.arch.persistence.room.*
import com.snt.phoney.domain.model.User
import io.reactivex.Flowable

@Dao
interface UserDao {
    @Query("SELECT * FROM USER WHERE id = :id")
    fun getById(id: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(user: User)

    @Query("DELETE FROM User WHERE id = :id")
    fun deleteById(id: String)

    @Update
    fun update(user: User)
}