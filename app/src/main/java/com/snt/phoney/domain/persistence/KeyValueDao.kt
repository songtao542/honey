package com.snt.phoney.domain.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.snt.phoney.domain.model.KeyValue

@Dao
interface KeyValueDao {

    @Query("SELECT * FROM KeyValue WHERE `key` = :key")
    fun get(key: String): KeyValue?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(kv: KeyValue)

    @Query("DELETE FROM KeyValue WHERE `key` = :key")
    fun remove(key: String)

    @Query("DELETE FROM KeyValue")
    fun removeAll()
}

fun KeyValueDao.put(key: String, value: String) {
    put(KeyValue(key, value))
}