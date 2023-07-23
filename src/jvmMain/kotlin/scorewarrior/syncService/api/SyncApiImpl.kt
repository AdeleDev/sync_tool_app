package scorewarrior.syncService.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.multipart.MultipartFile
import scorewarrior.syncService.api.dto.HeroData
import scorewarrior.syncService.api.dto.WeaponData
import scorewarrior.syncService.exception.ElementAlreadyExistException
import scorewarrior.syncService.exception.ElementNotExistException
import scorewarrior.syncService.exception.InvalidNameException
import scorewarrior.syncService.service.SyncServiceImpl
import scorewarrior.syncService.service.api.SyncService
import scorewarrior.syncservice.api.ElementApi
import scorewarrior.syncservice.model.AddElement201ResponseDto
import scorewarrior.syncservice.model.AddElementRequestDto
import scorewarrior.syncservice.model.UpdateElementRequestDto
import javax.validation.Valid

@Component
@RequestMapping("/v1")
@CrossOrigin(origins = ["\${client.address}"])
class SyncApiImpl : ElementApi {
    private var service: SyncService? = null

    @Autowired
    fun setService(service: SyncServiceImpl?) {
        this.service = service
    }

    override fun addElement(
        type: String?,
        name: String?,
        mainImage: MultipartFile?,
        icon: MultipartFile?,
        entireIcon: MultipartFile?,
        brokenIcon: MultipartFile?
    ): ResponseEntity<AddElement201ResponseDto> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(service?.addElement(type!!, name!!))
        } catch (e: ElementAlreadyExistException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build()
        }
    }

    override fun deleteDraftElement(
        type: @Valid String,
        name: @Valid String,
        userId: @Valid Long
    ): ResponseEntity<Void> {
        return try {
            service?.deleteDraftElement(name, type, userId)
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        } catch (e: ElementNotExistException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    override fun getAllElements(type: @Valid String): ResponseEntity<List<String>> {
        return try {
            val elements: List<String>? = service?.getAllEntities(type)
            ResponseEntity.status(HttpStatus.OK).body(elements)
        } catch (e: ElementNotExistException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        }
    }


    override fun getDraftElementByName(
        type: @Valid String,
        name: @Valid String,
        userId: @Valid Long
    ): ResponseEntity<AddElementRequestDto<Any>> {
        return try {
            val element = service?.getDraftElementByName(name, type, userId)
            ResponseEntity.status(HttpStatus.OK).contentType(MediaType.MULTIPART_FORM_DATA).body(element)
        } catch (e: ElementNotExistException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: InvalidNameException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    override fun getElementByName(
        type: @Valid String,
        name: @Valid String
    ): ResponseEntity<AddElementRequestDto<Any>> {
        return try {
            val element = service?.getElementByName(name, type)
            ResponseEntity.status(HttpStatus.OK).contentType(MediaType.MULTIPART_FORM_DATA).body(element)
        } catch (e: ElementNotExistException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: InvalidNameException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    override fun updateDraftElement(
        type: String?,
        userId: Long?,
        name: String?,
        mainImage: MultipartFile?,
        icon: MultipartFile?,
        entireIcon: MultipartFile?,
        brokenIcon: MultipartFile?
    ): ResponseEntity<Void> {
        return try {
            if (type == ElementTypes.HERO.value) {
                val heroDto = HeroData(name!!, mainImage, icon)
                service?.updateDraftElement(heroDto, type, userId!!)

            } else {
                val weaponDto = WeaponData(name!!, mainImage, entireIcon, brokenIcon)
                service?.updateDraftElement(weaponDto, type!!, userId!!)
            }
            ResponseEntity.ok().build()
        } catch (e: ElementNotExistException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: ElementAlreadyExistException) {
            ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build()
        }
    }

    override fun updateElement(type: String, body: UpdateElementRequestDto): ResponseEntity<Void> {

        return try {
            service?.updateElement(body.name, type, body.userId)
            ResponseEntity.ok().build()
        } catch (e: ElementNotExistException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: ElementAlreadyExistException) {
            ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build()
        }
    }
}
