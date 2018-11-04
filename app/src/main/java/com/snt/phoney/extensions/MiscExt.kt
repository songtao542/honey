package com.snt.phoney.extensions

/**
 * Extension property to get the TAG name for all object
 */
val <T : Any> T.TAG: String
    get():String = "SANYOU:${this::class.simpleName}"

