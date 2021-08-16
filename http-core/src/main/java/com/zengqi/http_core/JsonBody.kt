package com.zengqi.http_core

import com.google.gson.Gson
import java.util.*
import kotlin.collections.HashMap

class JsonBody(private val names: MutableList<String>, private val values: MutableList<Any>) :
    RequestBody() {

    override fun writeToBuffer(): ByteArray {
        val map = HashMap<String, Any>()
        names.forEachIndexed { index, s ->
            map[s] = values[index]
        }
        return Gson().toJson(map).toByteArray()
    }

    class Builder {
        private val names: MutableList<String> = ArrayList()
        private val values: MutableList<Any> = ArrayList()
        fun add(var1: String, var2: Any): Builder {
            names.add(var1)
            values.add(var2)
            return this
        }

        fun setMapParams(params: java.util.HashMap<String, String>): Builder {
            for ((key, value) in params) {
                names.add(key)
                values.add(value)
            }
            return this
        }

        fun build(): JsonBody {
            return JsonBody(names, values)
        }
    }
}