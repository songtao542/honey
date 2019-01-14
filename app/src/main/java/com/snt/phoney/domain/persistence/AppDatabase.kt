package com.snt.phoney.domain.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.snt.phoney.domain.model.*

@Database(entities = [User::class, KeyValue::class, Province::class, City::class, Photo::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun keyValueDao(): KeyValueDao
    abstract fun provinceCityDao(): ProvinceCityDao

    abstract fun photoDao(): PhotoDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java, "snt.db").build().also { INSTANCE = it }
                }
    }
}