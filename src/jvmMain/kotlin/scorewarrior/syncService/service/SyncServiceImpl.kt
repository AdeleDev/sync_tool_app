package scorewarrior.syncService.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import scorewarrior.syncService.api.ElementTypes
import scorewarrior.syncService.entity.HeroEntity
import scorewarrior.syncService.entity.WeaponEntity
import scorewarrior.syncService.exception.ElementAlreadyExistException
import scorewarrior.syncService.exception.ElementNotExistException
import scorewarrior.syncService.exception.InvalidNameException
import scorewarrior.syncService.mappers.FieldsMapper
import scorewarrior.syncService.repository.HeroRepository
import scorewarrior.syncService.repository.WeaponRepository
import scorewarrior.syncService.service.api.SyncService
import scorewarrior.syncservice.model.AddElement201ResponseDto
import scorewarrior.syncservice.model.GetAllElements200ResponseInnerDto
import scorewarrior.syncservice.model.HeroDto
import scorewarrior.syncservice.model.WeaponDto
import java.time.OffsetDateTime
import java.util.*

@Component
@Transactional
class SyncServiceImpl : SyncService {

    @Autowired
    lateinit var heroRepository: HeroRepository

    @Autowired
    lateinit var weaponRepository: WeaponRepository

    @Autowired
    private lateinit var fieldsMapper: FieldsMapper


    @Throws(ElementAlreadyExistException::class)
    override fun addElement(type: String, name: String): AddElement201ResponseDto? {
        LOGGER.info("Got create request for element type of $type with name = $name")

        if (type == ElementTypes.HERO.value) {
            val heroEntities: Optional<HeroEntity?> = heroRepository.findById(name)
            if (!heroEntities.isEmpty) {
                throw ElementAlreadyExistException(name)
            }
            val heroEntity = HeroEntity(name, null, null)
            heroEntity.createdTimestamp = OffsetDateTime.now()
            heroEntity.lastModifiedTimestamp = OffsetDateTime.now()
            heroRepository.save(heroEntity)
            LOGGER.info("Hero with name = $name was created")
            return AddElement201ResponseDto(heroEntity.name)
        } else {
            val weaponEntities: Optional<WeaponEntity?> = weaponRepository.findById(name)
            if (!weaponEntities.isEmpty) {
                throw ElementAlreadyExistException(name)
            }
            val weaponEntity = WeaponEntity(name, null, null, null)
            weaponEntity.createdTimestamp = OffsetDateTime.now()
            weaponEntity.lastModifiedTimestamp = OffsetDateTime.now()
            weaponRepository.save(weaponEntity)
            LOGGER.info("Weapon with name = $name was created")
            return AddElement201ResponseDto(weaponEntity.name)
        }
    }

    @Throws(ElementNotExistException::class, ElementAlreadyExistException::class)
    override fun updateDraftElement(dto: Any, type: String, user_id: Long) {
        if (type == ElementTypes.HERO.value) {
            dto as HeroDto
            LOGGER.info("Got draft update request from user = $user_id for element type of $type with name = " + dto.name)
            val heroEntities: Optional<HeroEntity?> = heroRepository.findById(dto.name)
            if (heroEntities.isEmpty) {
                throw ElementNotExistException()
            }
            val heroEntity: HeroEntity = heroEntities.get()
            val draftHeroEntity: HeroEntity = fieldsMapper.heroDtoToEntity(dto)

            heroEntity.lastModifiedTimestamp = OffsetDateTime.now()
            heroEntity.drafts.add(draftHeroEntity)
            draftHeroEntity.userId = user_id
            heroEntity.let { heroRepository.save(it) }
            LOGGER.info("Hero with name  = " + dto.name + " was updated with draft version from user = $user_id")
        } else {
            dto as WeaponDto
            LOGGER.info("Got draft update request from user = $user_id for element type of $type with name = " + dto.name)
            val weaponEntities: Optional<WeaponEntity?> = weaponRepository.findById(dto.name)
            if (weaponEntities.isEmpty) {
                throw ElementNotExistException()
            }
            val weaponEntity: WeaponEntity = weaponEntities.get()
            val draftWeaponEntity: WeaponEntity = fieldsMapper.weaponDtoToEntity(dto)

            weaponEntity.lastModifiedTimestamp = OffsetDateTime.now()
            draftWeaponEntity.userId = user_id
            weaponEntity.drafts.add(draftWeaponEntity)

            weaponEntity.let { weaponRepository.save(it) }
            LOGGER.info("Weapon with name  = " + dto.name + " was updated with draft version from user = $user_id")
        }
    }

    @Throws(ElementNotExistException::class, ElementAlreadyExistException::class)
    override fun updateElement(dto: Any, type: String) {
        if (type == ElementTypes.HERO.value) {
            dto as HeroDto
            LOGGER.info("Got update request for element type of $type with name = " + dto.name)
            val heroEntities: Optional<HeroEntity?> = heroRepository.findById(dto.name)
            if (heroEntities.isEmpty) {
                throw ElementNotExistException()
            }
            val heroEntity: HeroEntity = fieldsMapper.heroDtoToEntity(dto)
            heroEntity.lastModifiedTimestamp = OffsetDateTime.now()
            heroEntity.let { heroRepository.save(it) }
            LOGGER.info("Hero with name  = " + dto.name + " was updated")
        } else {
            dto as WeaponDto
            LOGGER.info("Got update request for element type of $type with name = " + dto.name)
            val weaponEntities: Optional<WeaponEntity?> = weaponRepository.findById(dto.name)
            if (weaponEntities.isEmpty) {
                throw ElementNotExistException()
            }
            val weaponEntity: WeaponEntity = fieldsMapper.weaponDtoToEntity(dto)
            weaponEntity.lastModifiedTimestamp = OffsetDateTime.now()
            weaponEntity.let { weaponRepository.save(it) }
            LOGGER.info("Weapon with name  = " + dto.name + " was updated")
        }
    }

    @Throws(ElementNotExistException::class)
    override fun deleteDraftElement(elementName: String, type: String, user_id: Long) {
        LOGGER.info("Got draft delete request for element type of $type with name = $elementName")
        if (type == ElementTypes.HERO.value) {
            val heroEntities: Optional<HeroEntity?> = heroRepository.findById(elementName)
            if (heroEntities.isEmpty) {
                throw ElementNotExistException()
            }
            val heroEntity: HeroEntity = heroEntities.get()
            heroEntity.lastModifiedTimestamp = OffsetDateTime.now()
            heroEntity.userId = null
            heroEntity.let { heroRepository.save(it) }
            LOGGER.info("Draft hero info from user = $user_id for element with name  = $elementName was deleted")
        } else {
            val weaponEntities: Optional<WeaponEntity?> = weaponRepository.findById(elementName)
            if (weaponEntities.isEmpty) {
                throw ElementNotExistException()
            }
            val weaponEntity: WeaponEntity = weaponEntities.get()
            weaponEntity.lastModifiedTimestamp = OffsetDateTime.now()
            weaponEntity.userId = null
            weaponEntity.let { weaponRepository.save(it) }
            LOGGER.info("Draft weapon info from user = $user_id for element with name  = $elementName was deleted")
        }
    }

    override fun getAllEntities(type: String): List<GetAllElements200ResponseInnerDto> {
        LOGGER.info("Got get request for all elements of type = $type")
        return if (type == ElementTypes.HERO.value) {
            val heroEntities: MutableList<HeroEntity?> = heroRepository.findAll()
            if (heroEntities.isEmpty()) {
                throw ElementNotExistException()
            }
            heroEntities.stream()
                .map { el -> el?.let { fieldsMapper.heroEntityToDto(it) } }.toList()
        } else {
            val weaponEntities: MutableList<WeaponEntity?> = weaponRepository.findAll()
            if (weaponEntities.isEmpty()) {
                throw ElementNotExistException()
            }
            weaponEntities.stream()
                .map { el -> el?.let { fieldsMapper.weaponEntityToDto(it) } }.toList()
        }

    }

    @Throws(ElementNotExistException::class, InvalidNameException::class)
    override fun getDraftElementByName(
        elementName: String,
        type: String,
        user_id: Long
    ): GetAllElements200ResponseInnerDto? {
        LOGGER.info("Got get request for draft element of type = $type with id = $elementName from user = $user_id")
        if (elementName.isEmpty() || elementName.isBlank()) {
            throw InvalidNameException()
        }

        return if (type == ElementTypes.HERO.value) {
            val heroEntities: Optional<HeroEntity?> = heroRepository.findById(elementName)
            if (heroEntities.isEmpty) {
                throw ElementNotExistException()
            }
            fieldsMapper.heroEntityToDto(heroRepository.findDraftByNameAndUser(elementName, user_id))
        } else {
            val weaponEntities: Optional<WeaponEntity?> = weaponRepository.findById(elementName)
            if (weaponEntities.isEmpty) {
                throw ElementNotExistException()
            }
            fieldsMapper.weaponEntityToDto(weaponRepository.findDraftByNameAndUser(elementName, user_id))
        }
    }

    @Throws(ElementNotExistException::class, InvalidNameException::class)
    override fun getElementByName(elementName: String, type: String): GetAllElements200ResponseInnerDto? {
        LOGGER.info("Got get request for element of type = $type with id = $elementName")
        if (elementName.isEmpty() || elementName.isBlank()) {
            throw InvalidNameException()
        }

        return if (type == ElementTypes.HERO.value) {
            val heroEntities: Optional<HeroEntity?> = heroRepository.findById(elementName)
            if (heroEntities.isEmpty) {
                throw ElementNotExistException()
            }
            fieldsMapper.heroEntityToDto(heroEntities.get())
        } else {
            val weaponEntities: Optional<WeaponEntity?> = weaponRepository.findById(elementName)
            if (weaponEntities.isEmpty) {
                throw ElementNotExistException()
            }
            fieldsMapper.weaponEntityToDto(weaponEntities.get())
        }

    }


    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(SyncServiceImpl::class.java)
    }
}