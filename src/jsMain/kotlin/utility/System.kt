package utility

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import model.serialization.createJsonFormatter
import kotlin.js.Date

actual fun getCurrentTimeMillis(): Long {
    return Date().getTime().toLong()
}


actual fun getHttpClient(): HttpClient = HttpClient(Js) {
    install(JsonFeature) {
        serializer = KotlinxSerializer(
            json = createJsonFormatter() // use common JSON config.
        )
    }
}

actual fun getIoDispatcher(): CoroutineDispatcher = Dispatchers.Default