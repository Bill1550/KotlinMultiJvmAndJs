package console

import di.ServiceLocator
import kotlinx.coroutines.runBlocking

/**
 * Run jvm version from the command line.
 */
fun main() {
    println("Hello command line")

    printTime()
}

fun printTime() = runBlocking {

    println( ServiceLocator.timeRepo.getLocalTime().toString() )

}