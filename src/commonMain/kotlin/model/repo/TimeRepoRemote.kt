package model.repo

import io.ktor.client.request.get
import io.ktor.http.cio.websocket.FrameType.Companion.get
import io.ktor.utils.io.core.use
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import model.data.*
import utility.cache.ContextCacheMap
import utility.cache.TimeValidator
import utility.getCurrentTimeMillis
import utility.getHttpClient
import kotlin.coroutines.CoroutineContext

/**
 * A Time Repo who's source is on the remote server.
 */
class TimeRepoRemote : TimeRepo, CoroutineScope {

    /**
     * A cache to save the results uniquely.
     * (The data structure is all a kludge, not a good design.
     * Configured this way just to force polymorphic serialization.
     */
    private data class TimeData(val time: TimeApiResp, val date: DateApiResp, val zone: ZoneApiResp)
    private val timeCache = ContextCacheMap<String, TimeData, Long>(
        validator = TimeValidator(
            timeToLive = 3000L,   // Stale after 3 seconds.
            errorTimeout = 10000L,
            currentTimeSource = { getCurrentTimeMillis() }
        ),
        fetchingScope = this,
        fetcher = ::fetchTime
    )

    private val rootJob = Job()

    override val coroutineContext: CoroutineContext
        get() = rootJob //To change initializer of created properties use File | Settings | File Templates.

    override suspend fun getLocalTime(): TimeApiResp {
        // get w/o cache
        return withContext(Dispatchers.Default) {
            getHttpClient().use { client ->
                client.get<TimeApiResp>("http://127.0.0.1:8080/time")
            }
        }
    }

    override suspend fun getTimeDataSet(timeZone: String?): List<ApiResp> {
        return timeCache.get(timeZone ?: "utc").asList()
    }


    private suspend fun fetchTime( key: String): TimeData {
        return getHttpClient().use { client ->
            client.get<ResponseWrapper>("http://127.0.0.1:8080/time/${key}").data.let { respList ->
                TimeData(
                    time = requireNotNull(respList.findResp<TimeApiResp>()),
                    date = requireNotNull(respList.findResp<DateApiResp>()),
                    zone = requireNotNull(respList.findResp<ZoneApiResp>())
                )
            }
        }
    }

    private fun TimeData.asList(): List<ApiResp> {
        return listOf(
            time,
            date,
            zone
        )
    }

}