package com.zengqi.samplehttpclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zengqi.http_core.ApiException
import com.zengqi.http_core.HttpManager2
import com.zengqi.http_core.HttpRequestManager
import com.zengqi.http_core.callback.json.JsonCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        HttpRequestManager.init("YOUR_BASE_URL")
//        OkGo.getInstance()
//            .init(application)
//        val execute = OkGo.get<MyResponse<ListBean<User>>>("")
//            .execute(object :com.lzy.okgo.callback.Callback<MyResponse<ListBean<User>>>{
//
//            })


        btnGet.setOnClickListener {
            val params = HashMap<String, String>()
            params["page"] = "1"
            params["pageSize"] = "10"
//            HttpRequestManager.get<MyResponse<ListBean<Int>>>("/api/v1/subjects", params, {
//                tvGet.text = it.data.toString()
//            })
            HttpManager2.getInstance().get<MyResponse<ListBean<Int>>>(
                "/api/v1/subjects",
                params,
                {
                    tvGet.text = it.data.toString()
                },{

                })
//            HttpManager2.getInstance().get(
//                "/api/v1/subjects",
//                params,
//                object : JsonCallback<MyResponse<ListBean<Int>>>() {
//                    override fun onSuccess(data: MyResponse<ListBean<Int>>?) {
//                        tvGet.text = data.toString()
//                    }
//
//                    override fun onFailed(apiException: ApiException?) {
//
//                    }
//                })
        }

        btnForm.setOnClickListener {
            post1()
        }

        btnJson.setOnClickListener {
            post2()
        }
    }

    private fun post1() {
        val params = HashMap<String, String>()
        params["account"] = "zengqi"
        params["passwd"] = "123456"
        HttpRequestManager.postForm<User>("/api/v1/post1", params, {
            tvForm.text = it.toString()
        }, {
            tvForm.text = it.toString()
        })
    }

    private fun post2() {
        val params = HashMap<String, String>()
        params["name"] = "zengqi"
        HttpRequestManager.postJson<String>("/api/v1/post2", params, {
            tvJson.text = it.toString()
        }, {
            tvJson.text = it.toString()
        })
    }

    data class MyResponse<T>(val message: String, val code: Int, val data: T)

    data class ListBean<T>(val list: MutableList<T>, val page: Int, val pageSize: Int)

    data class User(val username: String, val passwd: String)
}