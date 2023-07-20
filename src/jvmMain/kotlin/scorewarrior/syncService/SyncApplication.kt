package scorewarrior.syncService

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SyncApplication

val LOGGER: Logger = LoggerFactory.getLogger(SyncApplication::class.java)
fun main(args: Array<String>) {
    runApplication<SyncApplication>(*args)
    LOGGER.debug("Start application")
}
