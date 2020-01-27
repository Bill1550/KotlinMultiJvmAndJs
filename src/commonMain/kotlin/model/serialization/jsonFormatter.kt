package model.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule
import model.data.ApiResp
import model.data.DateApiResp
import model.data.TimeApiResp
import model.data.ZoneApiResp

fun createJsonFormatter(): Json =
    Json(
        configuration =  JsonConfiguration.Stable.copy(
            prettyPrint = true,
            encodeDefaults = false,
            classDiscriminator = "type",
            useArrayPolymorphism = false
        ),
        context = SerializersModule {
            polymorphic(ApiResp::class) {
                TimeApiResp::class with TimeApiResp.serializer()
                DateApiResp::class with DateApiResp.serializer()
                ZoneApiResp::class with ZoneApiResp.serializer()
            }
        }
    )