package com.snt.phoney.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "KeyValue")
data class KeyValue(
        @PrimaryKey val key: String,
        val value: String? = null
)