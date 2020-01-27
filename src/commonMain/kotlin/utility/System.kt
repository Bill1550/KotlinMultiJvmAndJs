package utility

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Returns the current time from the arbitrary epoch.  Only delta times make sense.
 */
expect fun getCurrentTimeMillis(): Long

expect fun getHttpClient(): HttpClient

expect fun getIoDispatcher(): CoroutineDispatcher