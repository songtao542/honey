package com.snt.phoney.ui.main.home.friend

enum class FilterType(val value: Int) {
    NONE(-1),
    DEFAULT(0),  //  0 默认全部（按时间倒叙）
    NEWEST(1),   //  1 今日新人
    POPULAR(2),  //  2 人气最高
    NEAREST(3),  //  3 距离最近
    HOTTEST(4),  //  4 身材最好
    MORE(5),     //  5 更多筛选
    BYCITY(6);     //6 查找城市

    override fun toString(): String {
        return when (value) {
            0 -> "0"
            1 -> "1"
            2 -> "2"
            3 -> "3"
            4 -> "4"
            5 -> "5"
            6 -> "6"
            else -> ""
        }
    }
}