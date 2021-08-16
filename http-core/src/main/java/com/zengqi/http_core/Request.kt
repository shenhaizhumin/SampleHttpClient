package com.zengqi.http_core

/**
 * @Author zengqi
 * @Date   2021/8/15
 **/
class Request//    this.tags = Util.immutableMap(builder.tags)//    val tags: Map<Class<*>, Any>? = null
    (builder: Builder) {
    var url: String? = null
    var method: String? = null
    var headers: Map<String, String>? = null
    var params: Map<String, String>? = null
    var body: RequestBody? = null

    init {
        url = builder.url
        method = if (builder.method == null) {
            "get"
        } else {
            builder.method
        }
        headers = builder.headers
        body = builder.body
        params = builder.params
    }

    class Builder {
        var url: String? = null
        var method: String? = null
        var headers: HashMap<String, String>? = null
        var params: HashMap<String, String>? = null
        var body: RequestBody? = null

        fun url(url: String): Builder {
            this.url = url
            return this
        }

        fun method(method: String, body: RequestBody? = null): Builder {
            this.method = method
            if (method.toLowerCase() != "get") {
                this.body = body
            }
            return this
        }

        fun addHeader(key: String, value: String): Builder {
            if (headers == null) {
                headers = HashMap()
            }
            headers?.put(key, value)
            return this
        }

        fun addQueryParams(name: String, value: String): Builder {
            if (params == null) {
                params = HashMap()
            }
            params?.put(name, value)
            return this
        }

        fun setQueryParams(params: HashMap<String, String>): Builder {
            this.params = params
            return this
        }

        fun post(body: RequestBody? = null): Builder {
            this.body = body
            this.method = "post"
            return this
        }

        fun build(): Request {
            return Request(this)
        }
    }
}