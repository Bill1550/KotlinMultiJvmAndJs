package utility.cache

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import utility.getCurrentTimeMillis

/**
 * A suspending cache with all the features:
 *   Lets fetches complete even if requesting coroutine is cancelled.
 *   Forwards errors to subsequent callers.
 *   Ensures freshness.
 *
 */
class UncancellableCacheMap<in K: Any, T: Any?>(

        /**
         * Time that the data will be considered valid in ms.
         */
        val timeToLive: Long = Long.MAX_VALUE,

        /**
         * When a fetch returns an error, time after which that the
         * fetch will not be retried bug the error will be returned.
         */
        val errorTimeout: Long = 0,

        /**
         * Coroutine scope that the fetcher is run in.  Allows the fetcher to keep running
         * even if the coroutine calling get is cancelled.
         */
        val fetchingScope: CoroutineScope,

        val currentTimeSource: ()->Long =  { getCurrentTimeMillis() },

        /**
         * Suspending function that fetches data.
         */
        val fetcher:  suspend (K)->T

): CacheMap<K, T> {

    private sealed class Entry<T>( val loadTime: Long ){
        class Error<T>(  loadTime: Long, val error: Throwable ) : Entry<T>( loadTime )
        class Data<T>( loadTime: Long, val data: T) : Entry<T>( loadTime )
    }

    private val map = mutableMapOf<K, Entry<T>>()
    private val mutex = Mutex()
    private var fetchingJob: Job? = null

    override suspend fun get( key: K ): T {
        val now = currentTimeSource()
        return (map[key]?.takeIf {it.isFresh(now)} ?: let {
            mutex.lock()
            map[key]?.takeIf{it.isFresh(now)}?.let {
                // appeared while we were waiting for mutex
                mutex.unlock()
                it
            } ?: let {
                // need to fetch
                map.remove(key) // make sure cache entry is clear (in case it was stale)
                fetchingJob = fetchingScope.launch {
                    try {
                        map[key] = Entry.Data(currentTimeSource(), fetcher(key))
                    } catch (t: Throwable) {
                        map[key] = Entry.Error(currentTimeSource(), t)
                    } finally {
                        mutex.unlock()
                    }
                }.apply { join() }
                map[key]
            }
        } ).let { entry ->
            if ( entry == null)
                throw CancellationException("null entry")

            entry.dispatch()
        }
    }

    override suspend fun clear() {
        fetchingJob?.cancel()
        mutex.withLock {
            map.clear()
        }
    }

    private fun Entry<T>.dispatch(): T {
        return when ( this ) {
            is Entry.Data -> data
            is Entry.Error -> throw error
        }
    }

    private fun Entry<T>.isFresh(now: Long): Boolean {
//        return true
        return when ( this ) {
            is Entry.Data -> now - this.loadTime < timeToLive
            is Entry.Error -> now - this.loadTime < errorTimeout
        }
    }
}