package utility

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.system.measureTimeMillis


actual fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

actual fun getHttpClient(): HttpClient {
    TODO("Needs to be implemented")
}

actual fun getIoDispatcher(): CoroutineDispatcher = Dispatchers.IO