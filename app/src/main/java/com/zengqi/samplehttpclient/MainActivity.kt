package com.zengqi.samplehttpclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zengqi.http_core.HttpRequestManager2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HttpRequestManager2.init("YOUR_BASE_URL")
        btnGet.setOnClickListener {
            val params = HashMap<String, String>()
            params["page"] = "1"
            params["pageSize"] = "10"
            HttpRequestManager2.get<String>("/api/v1/subjects", params, {
                tvGet.text = it
            })
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
        HttpRequestManager2.postForm<String>("/api/v1/post1", params, {
            tvForm.text = it.toString()
        }, {
            tvForm.text = it.toString()
        })
    }

    private fun post2() {
        val params = HashMap<String, String>()
        params["name"] = "zengqi"
        HttpRequestManager2.postJson<String>("/api/v1/post2", params, {
            tvJson.text = it.toString()
        }, {
            tvJson.text = it.toString()
        })
    }
}