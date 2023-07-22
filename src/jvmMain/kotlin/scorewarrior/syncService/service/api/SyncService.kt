package scorewarrior.syncService.service.api

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import scorewarrior.syncService.exception.ElementAlreadyExistException
import scorewarrior.syncService.exception.ElementNotExistException
import scorewarrior.syncservice.model.AddElement201ResponseDto
import scorewarrior.syncservice.model.UpdateElementRequestDto

@Component
@Transactional
interface SyncService {
    @Throws(ElementAlreadyExistException::class)
    fun addElement(type: String, name: String): AddElement201ResponseDto?

    @Throws(ElementAlreadyExistException::class)
    fun updateDraftElement(dto: Any, type: String, userId: Long)

    @Throws(ElementAlreadyExistException::class)
    fun updateElement(dto: Any, type: String)

    @Throws(ElementNotExistException::class)
    fun deleteDraftElement(elementName: String, type: String, userId: Long)

    @Throws(ElementNotExistException::class)
    fun getAllEntities(type: String): List<String>

    @Throws(ElementNotExistException::class)
    fun getDraftElementByName(elementName: String, type: String, userId: Long): UpdateElementRequestDto<Any>?

    @Throws(ElementNotExistException::class)
    fun getElementByName(elementName: String, type: String): UpdateElementRequestDto<Any>?
}