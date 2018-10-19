package com.snt.phoney.domain.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "KeyValue")
data class KeyValue(
        @PrimaryKey val key: String,
        val value: String? = null
)