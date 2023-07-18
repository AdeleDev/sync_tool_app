package scorewarrior.syncService.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import scorewarrior.syncService.exception.ElementNotExistException

@Component
class ElementNotExistExceptionHandler {
    @ExceptionHandler(value = [ElementNotExistException::class])
    fun toResponse(exception: ElementNotExistException): ResponseEntity<Any> {
        LOGGER.error(exception.message)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(ElementNotExistExceptionHandler::class.java)
    }
}