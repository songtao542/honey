package com.snt.phoney.domain.model

import androidx.room.Ignore
import kotlinx.serialization.Transient

open class Selectable {
    @Transient
    @Ignore
    var selected: Boolean = false
}