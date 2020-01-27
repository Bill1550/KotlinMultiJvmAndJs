package model.repo

import model.data.ApiResp
import model.data.ApiRespType
import model.data.TimeApiResp
import kotlin.reflect.KClass

interface TimeRepo {

    suspend fun getLocalTime(): TimeApiResp

    suspend fun getTimeDataSet( timeZone: String? ): List<ApiResp>
}