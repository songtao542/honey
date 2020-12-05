package com.snt.phoney.api

import java.util.*

/**
 * Author:         songtao
 * CreateDate:     2020/11/6 10:53
 * 用于测试的图片
 */
object MockPictures {
    private const val p0 = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=316507960,3288308609&fm=26&gp=0.jpg"
    private const val p1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607171811792&di=197bfa8636f52d629014808fde3a213b&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F7%2Fe9%2Ff3ec791448.jpg"
    private const val p2 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3254403927,3182508607&fm=11&gp=0.jpg"
    private const val p3 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607171811790&di=aa0df2820f261cfc11927d773c579094&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F0%2F2b%2F2a6a802289.jpg"
    private const val p4 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3463793294,1071483026&fm=26&gp=0.jpg"
    private const val p5 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607171811782&di=dce3755f5756e71534d659b83098c376&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170821%2F2135x0nxm1yd0vt.jpg"
    private const val p6 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607171811781&di=23fa4c82b6911ed27039ef4356e9f55c&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F9%2F05%2Ff6e9719174.jpg"
    private const val p7 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607171811780&di=c23fcecb67b4d207a13d01542f1fa211&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F7%2Fe9%2Ff3ec791450.jpg"
    private const val p8 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607171811776&di=195631ceef9ea97fe433a57327afe63f&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F7%2Fe9%2Ff3ec791444.jpg"
    private const val p9 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607171811771&di=214222e73f29e86d3827d36e47fb464b&imgtype=0&src=http%3A%2F%2Fimg1.juimg.com%2F150705%2F330419-150F51Q11880.jpg"
    private const val p10 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607171916221&di=8399e247b03770e520a343454748e61d&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F5%2Fd8%2F78b7776720.jpg"
    private const val p11 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607171916219&di=59e97d46c1c961c4a803e45946d3333c&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F171010%2F147340qgdcoexd.jpg"
    private const val p12 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607171916218&di=d025cdce039964e6e60c24e5e2ce2b85&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2Fd%2F5a%2F1c4b329886.jpg"
    private const val p13 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2996082579,3657875820&fm=26&gp=0.jpg"
    private const val p14 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607172224643&di=3640e539041d11061ee2284a16a785ae&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170615%2F11152G422-5.jpg"
    private const val p15 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607172224641&di=fabce36e4710b21e8dfaf04a0ac376c7&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F3%2Fdd%2F9bcf1368225.jpg"
    private const val p16 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607172224693&di=4d2388f8e7f165134a5b71c0141e794c&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170615%2F11152G219-1.jpg"
    private const val p17 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607172278259&di=b441870be0afe985f7a52e6ac273e7ed&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F3%2F9b%2F0afc416274.jpg"
    private const val p18 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607172340520&di=7602317958fe66634d41800ec7b0dcc0&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn%2Fw1147h720%2F20180102%2F172e-fyqefvx2771455.png"
    private const val p19 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2498542006,2683728467&fm=26&gp=0.jpg"

    private val pArray = arrayOf(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10,
            p11, p12, p13, p14, p15, p16, p17, p18, p19)

    private val random = Random()

    fun random(): String {
        return pArray[random.nextInt(20)]
    }

    operator fun get(i: Int): String {
        return pArray[i % 20]
    }
}