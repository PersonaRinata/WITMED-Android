package com.handsome.module.chat.bean

import com.handsome.lib.util.network.IApiWrapper

data class SumDiseaseBean(
    val msg: String,
    override val status_code: Int,
    override val status_msg: String
) : IApiWrapper