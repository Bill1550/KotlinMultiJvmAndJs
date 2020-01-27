package model.repo

import kotlinx.coroutines.delay
import model.data.*
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.*
import kotlin.reflect.KClass

class TimeRepoSource : TimeRepo {


    override suspend fun getLocalTime(): TimeApiResp {
//        delay(500) // simulate a slow data source.
        return LocalTime.now().let {
            TimeApiResp(
                hour = it.hour,
                min = it.minute,
                sec = it.second
            )
        }
    }

    override suspend fun getTimeDataSet(zoneParam: String?): List<ApiResp> {
        requireNotNull(zoneParam)
        val zoneId = when(zoneParam.toUpperCase()){
            "UTC" -> ZoneId.of("UTC")
            else -> throw IllegalArgumentException("Unrecognized time zone parameter: $zoneParam")
        }

        return ZonedDateTime.ofInstant( Instant.now(), zoneId).let{ t ->

            listOf(
                TimeApiResp( t.hour, t.minute, t.second ),
                DateApiResp( t.year, t.monthValue, t.dayOfMonth ),
                ZoneApiResp( t.zone.id, t.zone.getDisplayName(TextStyle.FULL, Locale.US))
            )
        }
    }

}