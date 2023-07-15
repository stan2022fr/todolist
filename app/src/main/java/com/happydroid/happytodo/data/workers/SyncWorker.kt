package com.happydroid.happytodo.data.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.happydroid.happytodo.data.repository.TodoItemsRepository
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * A worker class responsible for updating data on the server when the device is connected.
 */
class SyncWorker (appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    @Inject
    lateinit var todoItemsRepository: TodoItemsRepository

    override fun doWork(): Result {
        try {
            runBlocking {
                todoItemsRepository.saveAllTodoItemsToRemote()
                todoItemsRepository.fetchFromRemoteApi()
            }
            return Result.success()
        } catch (e: Exception) {
            Log.e("DataUpdateWorker", "Exception: ${e.message}")
            return Result.failure()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(appContext: Context, params: WorkerParameters): NotificationWorker
    }
}

