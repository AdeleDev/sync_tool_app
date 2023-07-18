package scorewarrior.syncService.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import java.net.SocketTimeoutException

@Component
class SocketTimeoutExceptionHandler {
    @ExceptionHandler(value = [RuntimeException::class])
    fun toResponse(exception: RuntimeException): ResponseEntity<Any> {
        LOGGER.error(exception.message)
        return if (exception.cause is SocketTimeoutException) {
            ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build()
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(SocketTimeoutExceptionHandler::class.java)
    }
}