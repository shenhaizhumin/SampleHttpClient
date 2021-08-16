package com.zengqi.http_core

import java.util.concurrent.TimeUnit

/**
 * @Author zengqi
 * @Date   2021/8/15
 **/
internal class Timeout(
    var timeOutValue: Int = 5 * 1000,
    var timeUnit: TimeUnit = TimeUnit.SECONDS
)
//    /**
//     * True if `deadlineNanoTime` is defined. There is no equivalent to null
//     * or 0 for [System.nanoTime].
//     */
//    private var hasDeadline = false
//    private var deadlineNanoTime: Long = 0
//    private var timeoutNanos: Long = 0
//
//    /**
//     * Wait at most `timeout` time before aborting an operation. Using a
//     * per-operation timeout means that as long as forward progress is being made,
//     * no sequence of operations will fail.
//     *
//     *
//     * If `timeout == 0`, operations will run indefinitely. (Operating
//     * system timeouts may still apply.)
//     */
//    fun timeout(timeout: Long, unit: TimeUnit?): Timeout {
//        require(timeout >= 0) { "timeout < 0: $timeout" }
//        requireNotNull(unit) { "unit == null" }
//        timeoutNanos = unit.toNanos(timeout)
//        return this
//    }
//
//    /** Returns the timeout in nanoseconds, or `0` for no timeout.  */
//    fun timeoutNanos(): Long {
//        return timeoutNanos
//    }
//
//    /** Returns true if a deadline is enabled.  */
//    fun hasDeadline(): Boolean {
//        return hasDeadline
//    }
//
//    /**
//     * Returns the [nano time][System.nanoTime] when the deadline will
//     * be reached.
//     *
//     * @throws IllegalStateException if no deadline is set.
//     */
//    fun deadlineNanoTime(): Long {
//        check(hasDeadline) { "No deadline" }
//        return deadlineNanoTime
//    }
//
//    /**
//     * Sets the [nano time][System.nanoTime] when the deadline will be
//     * reached. All operations must complete before this time. Use a deadline to
//     * set a maximum bound on the time spent on a sequence of operations.
//     */
//    fun deadlineNanoTime(deadlineNanoTime: Long): Timeout {
//        hasDeadline = true
//        this.deadlineNanoTime = deadlineNanoTime
//        return this
//    }
//
//    /** Set a deadline of now plus `duration` time.  */
//    fun deadline(duration: Long, unit: TimeUnit?): Timeout {
//        require(duration > 0) { "duration <= 0: $duration" }
//        requireNotNull(unit) { "unit == null" }
//        return deadlineNanoTime(System.nanoTime() + unit.toNanos(duration))
//    }
//
//    /** Clears the timeout. Operating system timeouts may still apply.  */
//    fun clearTimeout(): Timeout {
//        timeoutNanos = 0
//        return this
//    }
//
//    /** Clears the deadline.  */
//    fun clearDeadline(): Timeout {
//        hasDeadline = false
//        return this
//    }
//
//    /**
//     * Throws an [InterruptedIOException] if the deadline has been reached or if the current
//     * thread has been interrupted. This method doesn't detect timeouts; that should be implemented to
//     * asynchronously abort an in-progress operation.
//     */
//    @Throws(IOException::class)
//    fun throwIfReached() {
//        if (Thread.interrupted()) {
//            Thread.currentThread().interrupt() // Retain interrupted status.
//            throw InterruptedIOException("interrupted")
//        }
//        if (hasDeadline && deadlineNanoTime - System.nanoTime() <= 0) {
//            throw InterruptedIOException("deadline reached")
//        }
//    }
//
//    /**
//     * Waits on `monitor` until it is notified. Throws [InterruptedIOException] if either
//     * the thread is interrupted or if this timeout elapses before `monitor` is notified. The
//     * caller must be synchronized on `monitor`.
//     *
//     *
//     * Here's a sample class that uses `waitUntilNotified()` to await a specific state. Note
//     * that the call is made within a loop to avoid unnecessary waiting and to mitigate spurious
//     * notifications. <pre>`class Dice {
//     * Random random = new Random();
//     * int latestTotal;
//     *
//     * public synchronized void roll() {
//     * latestTotal = 2 + random.nextInt(6) + random.nextInt(6);
//     * System.out.println("Rolled " + latestTotal);
//     * notifyAll();
//     * }
//     *
//     * public void rollAtFixedRate(int period, TimeUnit timeUnit) {
//     * Executors.newScheduledThreadPool(0).scheduleAtFixedRate(new Runnable() {
//     * public void run() {
//     * roll();
//     * }
//     * }, 0, period, timeUnit);
//     * }
//     *
//     * public synchronized void awaitTotal(Timeout timeout, int total)
//     * throws InterruptedIOException {
//     * while (latestTotal != total) {
//     * timeout.waitUntilNotified(this);
//     * }
//     * }
//     * }
//    `</pre> *
//     */
//    @Throws(InterruptedIOException::class)
//    fun waitUntilNotified(monitor: Any) {
//        try {
//            val hasDeadline = hasDeadline()
//            val timeoutNanos = timeoutNanos()
//            if (!hasDeadline && timeoutNanos == 0L) {
//                monitor.wait() // There is no timeout: wait forever.
//                return
//            }
//
//            // Compute how long we'll wait.
//            val waitNanos: Long
//            val start = System.nanoTime()
//            waitNanos = if (hasDeadline && timeoutNanos != 0L) {
//                val deadlineNanos = deadlineNanoTime() - start
//                Math.min(timeoutNanos, deadlineNanos)
//            } else if (hasDeadline) {
//                deadlineNanoTime() - start
//            } else {
//                timeoutNanos
//            }
//
//            // Attempt to wait that long. This will break out early if the monitor is notified.
//            var elapsedNanos = 0L
//            if (waitNanos > 0L) {
//                val waitMillis = waitNanos / 1000000L
//                monitor.wait(waitMillis, (waitNanos - waitMillis * 1000000L).toInt())
//                elapsedNanos = System.nanoTime() - start
//            }
//
//            // Throw if the timeout elapsed before the monitor was notified.
//            if (elapsedNanos >= waitNanos) {
//                throw InterruptedIOException("timeout")
//            }
//        } catch (e: InterruptedException) {
//            Thread.currentThread().interrupt() // Retain interrupted status.
//            throw InterruptedIOException("interrupted")
//        }
//    }
//
//    companion object {
//        /**
//         * An empty timeout that neither tracks nor detects timeouts. Use this when
//         * timeouts aren't necessary, such as in implementations whose operations
//         * do not block.
//         */
//        val NONE: Timeout = object : Timeout() {
//            override fun timeout(timeout: Long, unit: TimeUnit?): Timeout {
//                return this
//            }
//
//            override fun deadlineNanoTime(deadlineNanoTime: Long): Timeout {
//                return this
//            }
//
//            @Throws(IOException::class)
//            override fun throwIfReached() {
//            }
//        }
//
//        fun minTimeout(aNanos: Long, bNanos: Long): Long {
//            if (aNanos == 0L) return bNanos
//            if (bNanos == 0L) return aNanos
//            return if (aNanos < bNanos) aNanos else bNanos
//        }
//    }
//}