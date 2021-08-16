package com.zengqi.http_core

object ResponseCode {
    /**
     * 成功code
     */
    const val SUCCESSFUL_CODE = 1

    /**
     * 特殊状态码，需要执行退出登录操作
     */
    const val ERROR_TOKEN_996 = 996

    const val ERROR_TOKEN_997 = 997

    const val ERROR_TOKEN_999 = 999

    const val ERROR_TOKEN_FIELD = 1003

    const val ERROR_NOT_LOGIN = 1004

    const val ERROR_OTHER_DEVICE_LOGIN = 1028

    //用户一小时内支付过
    const val ERROR_888 = 888

    //支付密码未设定
    const val ERROR_1020 = 1020

    //支付密码错误
    const val ERROR_697 = 697

    //拼多多未授权
    const val ERROR_20020 = 20020

    //链街：开通推荐位达到上限
    const val ERROR_5001 = 5001

    //链街：拼团超出活动时间
    const val ERROR_20041 = 20041


    const val CODE_1074 = 1074 //无换绑机会)

    const val CODE_1077 = 1077 //您的账号当前无剩余解绑机会，请联系客服)

    const val CODE_1078 = 1078 //当前账号还有{0}解绑机会，是否确定解绑)

}