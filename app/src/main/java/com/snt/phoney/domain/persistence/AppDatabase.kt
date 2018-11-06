package com.snt.phoney.domain.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.snt.phoney.domain.model.KeyValue
import com.snt.phoney.domain.model.User

@Database(entities = [User::class, KeyValue::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun keyValueDao(): KeyValueDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java, "demo.db").build().also { INSTANCE = it }
                }
    }
}