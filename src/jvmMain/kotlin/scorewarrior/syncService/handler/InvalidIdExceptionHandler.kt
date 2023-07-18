package scorewarrior.syncService.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.naming.InvalidNameException

@Component
class InvalidIdExceptionHandler {
    @ExceptionHandler(value = [InvalidNameException::class])
    fun toResponse(exception: InvalidNameException): ResponseEntity<Any> {
        LOGGER.error(exception.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(InvalidIdExceptionHandler::class.java)
    }
}