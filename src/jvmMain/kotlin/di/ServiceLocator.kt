package di

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule
import model.data.ApiResp
import model.data.DateApiResp
import model.data.TimeApiResp
import model.data.ZoneApiResp
import model.repo.TimeRepo
import model.repo.TimeRepoSource
import model.serialization.createJsonFormatter

object ServiceLocator {

    val timeRepo: TimeRepo by lazy { TimeRepoSource() }

    val jsonFormatter: Json by lazy { createJsonFormatter() }

}