package utility

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import model.serialization.createJsonFormatter


actual fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

actual fun getHttpClient(): HttpClient {
    return HttpClient(Apache){
        engine {
            this.connectTimeout = 5_000
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(
                json = createJsonFormatter() // use common JSON config.
            )
        }

    }
}

actual fun getIoDispatcher(): CoroutineDispatcher = Dispatchers.IO