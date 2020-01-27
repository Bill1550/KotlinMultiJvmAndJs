import di.ServiceLocator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.data.ApiResp
import model.data.DateApiResp
import model.data.TimeApiResp
import model.data.findResp
import sample.Sample
import sample.hello
import kotlin.browser.document

@Suppress("unused")
@JsName("helloWorld")
fun helloWorld(salutation: String) {
    val message = "$salutation from Kotlin.JS ${hello()}, check me value: ${Sample().checkMe()}"
    document.getElementById("js-response")?.textContent = message

    loadServerTime()
}

fun loadServerTime() {

    GlobalScope.launch {

        document.getElementById( "js-time-date")?.apply {
            textContent = "loading more..."
            textContent = try {
                readServerTimeAndDate("utc").let { list ->
                    list.findResp<DateApiResp>()?.let { date ->
                        "${date.year}-${date.month}-${date.day}"
                    }
                } ?: "null"
            } catch ( t: Throwable ){
                "error: ${t.message}"
            }
        }

        repeat(10) {
            delay(2000)
            document.getElementById( "js-time" )?.apply {
                textContent = "loading..."
                textContent = try {
                    readServerTimeAndDate("utc").findResp<TimeApiResp>()?.let { t ->
                        "${t.hour}:${t.min}:${t.sec}"
                    }
                } catch( t: Throwable ) {
                    "Error: ${t.message}"
                }
            }

            console.log("Repeated $it times")
        }


    }
}

suspend fun readServerTimeWithClient(): TimeApiResp {
    return ServiceLocator.timeRepo.getLocalTime()
}

suspend fun readServerTimeAndDate( zone: String): List<ApiResp> {
    return ServiceLocator.timeRepo.getTimeDataSet(zone)
}

fun main() {
    document.addEventListener("DOMContentLoaded", {
        helloWorld("Hi!")
    })
}
