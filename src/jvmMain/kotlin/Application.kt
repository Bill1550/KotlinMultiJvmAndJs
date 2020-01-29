import di.ServiceLocator
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.serialization
import kotlinx.html.*
import model.data.ErrorApiResp
import model.data.ResponseWrapper
import sample.Sample
import sample.hello

fun Application.mainModule(testing: Boolean = false ) {

    install(ContentNegotiation) {
        serialization(
            contentType = ContentType.Application.Json,
            json = ServiceLocator.jsonFormatter
        )
    }

    install(StatusPages) {

        exception<IllegalStateException> { ex ->
            call.respond(
                status = HttpStatusCode.NotAcceptable,
                message = ErrorApiResp( code = 101, message=ex.message?:"")
            )
        }

        exception<IllegalArgumentException> { ex ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message =  ErrorApiResp( code=102, message = ex.message?: "" )
            )
        }
    }

    routing {
        get("/") {
            call.respondHtml {
                head {
                    title("Hello from Ktor!")
                }
                body {
                    +"${hello()} from Ktor. Check me value: ${Sample().checkMe()}"
                    div {
                        id = "js-response"
                        +"Loading..."
                    }
                    br {  }
                    div {
                        id = "js-time"
                        +"????"
                    }
                    br {}
                    div {
                        id="js-time-date"
                        +"????"
                    }
                    br {}
                     div {
                        id="js-call-kotlin"
                        onClick="alert(JVMandJS.sample.getMessage())"
                        +"Click here!"
                    }

                    script(src = "/static/JVMandJS.js") {}
                }
            }
        }

        get( "/time") {
            call.response.header("Access-Control-Allow-Origin", "*")
            call.respond( ServiceLocator.timeSource.getLocalTime() )
        }

        get( "/time/{zone}") {
            call.response.header( "Access-Control-Allow-Origin", "*")
            call.respond( ServiceLocator.timeSource.getTimeDataSet( call.parameters["zone"] ).let {
                ResponseWrapper( it )
            })
        }

        static("/static") {
            resource("JVMandJS.js")
        }
    }
}