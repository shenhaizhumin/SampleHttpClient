# SampleHttpClient


### step1:
HttpRequestManager.init("YOUR_BASE_URL")

### step2:
Do get request:
```
val params = HashMap<String, String>()
params["page"] = "1"
params["pageSize"] = "10"
HttpRequestManager.get<String>("/api/v1/subjects", params, {
    // there is main thread
    println(it)
})
```
Do post request:
```
val params = HashMap<String, String>()
params["account"] = "zengqi"
params["passwd"] = "123456"
HttpRequestManager.postForm<String>("/api/v1/post1", params, {
    tvForm.text = it.toString()
}, {
    // handle errors
    tvForm.text = it.toString()
})
```


### to be continued....
