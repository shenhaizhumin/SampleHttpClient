package com.zengqi.http_core

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class Dispatcher {
    private val mThreadPoolExecutor: ThreadPoolExecutor
    private val mQueue = LinkedBlockingQueue<Runnable>(100)

    init {
        mThreadPoolExecutor =
            ThreadPoolExecutor(3, 100, 10, TimeUnit.SECONDS, ArrayBlockingQueue(100),
                RejectedExecutionHandler { r, _ ->
                    mQueue.add(r)
                })
        mThreadPoolExecutor.execute {
            while (true) {
                val task = mQueue.take()
                mThreadPoolExecutor.execute(task)
            }
        }
    }

    fun addTask(r: Runnable) {
        mQueue.add(r)
    }

    fun addTask(newRealCall: RealCall) {
        mQueue.add(newRealCall)
    }
}