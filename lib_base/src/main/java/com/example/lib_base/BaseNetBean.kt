package com.example.lib_base

import java.io.Serializable

/**
 * Created by chene on @date 20-12-4 下午2:36
 */
open class BaseNetBean(
    var errorCode: String = "",
    var errorMsg: String = ""
) : Serializable