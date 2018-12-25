package com.snt.phoney.ui.main.square.official

enum class FilterTime(val value: Int) {
    NONE(-1),// 无
    ALL(0),// 所有
    TODAY(1),// 今天发布
    THREE_DAY(2),// 近三天发布
    SEVEN_DAY(3);// 近七天发布

    override fun toString(): String {
        return when (value) {
            0 -> "0"
            1 -> "1"
            2 -> "2"
            3 -> "3"
            else -> ""
        }
    }

    companion object {
        fun from(value: Int): FilterTime {
            return when (value) {
                0 -> ALL
                1 -> TODAY
                2 -> THREE_DAY
                3 -> SEVEN_DAY
                else -> NONE
            }
        }
    }
}

enum class FilterDistance(val value: Int) {
    NONE(-1),// 无
    ALL(0),//所有
    FIVE_KM(1),//五公里内
    TEN_KM(2),//十公里内
    OTHER(3);//其他

    override fun toString(): String {
        return when (value) {
            0 -> "0"
            1 -> "1"
            2 -> "2"
            3 -> "3"
            else -> ""
        }
    }

    companion object {
        fun from(value: Int): FilterDistance {
            return when (value) {
                0 -> ALL
                1 -> FIVE_KM
                2 -> TEN_KM
                3 -> TEN_KM
                else -> NONE
            }
        }
    }
}

enum class FilterContent(val value: Int, val content: String) {
    NONE(-1, ""),// 无
    ALL(0, "不限制"),//不限制
    EATING_SHOPPING(1, "吃饭逛街"),// 吃饭逛街
    SEE_FILM(2, "看电影"),//  看电影
    DRINK(3, "喝酒"),// 喝酒
    OTHER(4, "其他");//其他

    override fun toString(): String {
        return when (value) {
            0 -> "0"
            1 -> "1"
            2 -> "2"
            3 -> "3"
            4 -> "4"
            else -> ""
        }
    }

    companion object {
        fun from(value: Int): FilterContent {
            return when (value) {
                0 -> ALL
                1 -> EATING_SHOPPING
                2 -> SEE_FILM
                3 -> DRINK
                4 -> OTHER
                else -> NONE
            }
        }
    }
}