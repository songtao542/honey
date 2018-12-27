package com.snt.phoney.domain.model

enum class OrderType(val value: Int) {
    BUY_MIBI(0),
    BUY_VIP(1),
    USE_RED_ENVELOPE_MIBI(10),
    USE_UNLOCK_ALBUM_MIBI(11),
    USE_VOICE_MIBI(12),
    WITHDRAW_MIBI(31);

    companion object {
        fun from(value: Int): OrderType {
            return when (value) {
                0 -> BUY_MIBI
                1 -> BUY_VIP
                10 -> USE_RED_ENVELOPE_MIBI
                11 -> USE_UNLOCK_ALBUM_MIBI
                12 -> USE_VOICE_MIBI
                else -> WITHDRAW_MIBI
            }
        }
    }
}