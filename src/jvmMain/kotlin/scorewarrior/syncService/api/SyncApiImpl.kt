package scorewarrior.syncService.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import scorewarrior.syncService.exception.ElementAlreadyExistException
import scorewarrior.syncService.exception.ElementNotExistException
import scorewarrior.syncService.exception.InvalidNameException
import scorewarrior.syncService.service.SyncServiceImpl
import scorewarrior.syncService.service.api.SyncService
import scorewarrior.syncservice.api.ElementApi
import scorewarrior.syncservice.model.AddElement201ResponseDto
import scorewarrior.syncservice.model.GetAllElements200ResponseInnerDto
import scorewarrior.syncservice.model.HeroDto
import scorewarrior.syncservice.model.WeaponDto
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
        type: @Valid String,
        name: @Valid String,
        mainImage: @Valid Resource?,
        icon: @Valid Resource?,
        entireIcon: @Valid Resource?,
        brokenIcon: @Valid Resource?
    ): ResponseEntity<AddElement201ResponseDto> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(service?.addElement(type, name))
        } catch (e: ElementAlreadyExistException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build()
        }
    }

    override fun deleteDraftElement(
        type: @Valid String,
        name: @Valid String,
        userId: @Valid Long
    ): ResponseEntity<Unit> {
        return try {
            service?.deleteDraftElement(type, type, userId)
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        } catch (e: ElementNotExistException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    override fun getAllElements(type: @Valid String): ResponseEntity<List<GetAllElements200ResponseInnerDto>> {
        return try {
            val elements: List<GetAllElements200ResponseInnerDto>? = service?.getAllEntities(type)
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
    ): ResponseEntity<GetAllElements200ResponseInnerDto> {
        return try {
            val element = service?.getDraftElementByName(name, type, userId)
            ResponseEntity.status(HttpStatus.OK).body(element)
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
    ): ResponseEntity<GetAllElements200ResponseInnerDto> {
        return try {
            val element = service?.getElementByName(name, type)
            ResponseEntity.status(HttpStatus.OK).body(element)
        } catch (e: ElementNotExistException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: InvalidNameException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    override fun updateDraftElement(
        type: @Valid String,
        userId: @Valid Long,
        name: @Valid String,
        mainImage: @Valid Resource?,
        icon: @Valid Resource?,
        entireIcon: @Valid Resource?,
        brokenIcon: @Valid Resource?
    ): ResponseEntity<Unit> {
        return try {
            if (type == ElementTypes.HERO.value) {
                val heroDto = HeroDto(name, mainImage, icon)
                service?.updateDraftElement(heroDto, type, userId)

            } else {
                val weaponDto = WeaponDto(name, mainImage, entireIcon, brokenIcon)
                service?.updateDraftElement(weaponDto, type, userId)
            }
            ResponseEntity.ok().build()
        } catch (e: ElementNotExistException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: ElementAlreadyExistException) {
            ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build()
        }
    }

    override fun updateElement(
        type: @Valid String,
        name: @Valid String,
        mainImage: @Valid Resource?,
        icon: @Valid Resource?,
        entireIcon: @Valid Resource?,
        brokenIcon: @Valid Resource?
    ): ResponseEntity<Unit> {
        return try {
            if (type == ElementTypes.HERO.value) {
                val heroDto = HeroDto(name, mainImage, icon)
                service?.updateElement(heroDto, type)

            } else {
                val weaponDto = WeaponDto(name, mainImage, entireIcon, brokenIcon)
                service?.updateElement(weaponDto, type)
            }
            ResponseEntity.ok().build()
        } catch (e: ElementNotExistException) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: ElementAlreadyExistException) {
            ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build()
        }
    }
}