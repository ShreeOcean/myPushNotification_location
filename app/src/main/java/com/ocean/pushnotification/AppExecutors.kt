package com.ocean.pushnotification

import android.os.Looper
import java.util.concurrent.Executor
import android.os.Handler
import java.util.concurrent.Executors

class AppExecutors private constructor(
    private val diskIO : Executor,
    private val mainThread : Executor,
    private val networkIO : Executor,
){
    fun diskIO(): Executor{
        return diskIO
    }
    fun mainThread(): Executor{
        return mainThread
    }
    fun networkIO(): Executor{
        return networkIO
    }

    private class MainThreadExecutor : Executor{
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object{
        //for singleton instantiation
        private val LOCK = Any()
        private var sInstance : AppExecutors? = null
        val instance: AppExecutors?
            get() {
                if (sInstance == null) {
                    synchronized(LOCK) {
                        sInstance = AppExecutors(
                            Executors.newSingleThreadExecutor(),
                            Executors.newFixedThreadPool(3),
                            MainThreadExecutor()
                        )
                    }
                }
                return sInstance
            }
    }
}