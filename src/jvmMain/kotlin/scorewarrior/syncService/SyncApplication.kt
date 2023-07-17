package scorewarrior.syncService

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SyncApplication

val LOGGER: Logger = LoggerFactory.getLogger(SyncApplication::class.java)
fun main(args: Array<String>) {
    SpringApplication.run(SyncApplication::class.java, *args)
    LOGGER.debug("Start application")
}
