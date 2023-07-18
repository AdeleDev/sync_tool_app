package scorewarrior.syncService.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import scorewarrior.syncService.exception.ElementAlreadyExistException

@Component
class ElementAlreadyExistExceptionHandler {
    @ExceptionHandler(value = [ElementAlreadyExistException::class])
    fun toResponse(exception: ElementAlreadyExistException): ResponseEntity<Any> {
        LOGGER.error(exception.message)
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build()
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(ElementAlreadyExistExceptionHandler::class.java)
    }
}