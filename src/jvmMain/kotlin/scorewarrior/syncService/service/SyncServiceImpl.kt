package scorewarrior.syncService.service

import org.apache.tomcat.util.http.fileupload.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import scorewarrior.syncService.api.ElementTypes
import scorewarrior.syncService.api.dto.HeroData
import scorewarrior.syncService.api.dto.WeaponData
import scorewarrior.syncService.entity.HeroEntity
import scorewarrior.syncService.entity.ItemEntity
import scorewarrior.syncService.entity.WeaponEntity
import scorewarrior.syncService.exception.ElementAlreadyExistException
import scorewarrior.syncService.exception.ElementNotExistException
import scorewarrior.syncService.exception.InvalidNameException
import scorewarrior.syncService.mappers.FieldsMapper
import scorewarrior.syncService.repository.HeroRepository
import scorewarrior.syncService.repository.WeaponRepository
import scorewarrior.syncService.service.api.SyncService
import scorewarrior.syncservice.model.AddElement201ResponseDto
import scorewarrior.syncservice.model.AddElementRequestDto
import java.io.File
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
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
            heroRepository.save(heroEntity)
            LOGGER.info("Hero with name = $name was created")
            val response = AddElement201ResponseDto()
            response.link(heroEntity.name)
            return response
        } else {
            val weaponEntities: Optional<WeaponEntity?> = weaponRepository.findById(name)
            if (!weaponEntities.isEmpty) {
                throw ElementAlreadyExistException(name)
            }
            val weaponEntity = WeaponEntity(name, null, null, null)
            weaponRepository.save(weaponEntity)
            LOGGER.info("Weapon with name = $name was created")
            val response = AddElement201ResponseDto()
            response.link(weaponEntity.name)
            return response
        }
    }

    @Throws(ElementNotExistException::class, ElementAlreadyExistException::class)
    override fun updateDraftElement(dto: Any, type: String, userId: Long) {
        if (type == ElementTypes.HERO.value) {
            dto as HeroData
            LOGGER.info("Got draft update request from user = $userId for element type of $type with name = " + dto.name)
            val heroEntities: Optional<HeroEntity?> = heroRepository.findById(dto.name)
            if (heroEntities.isEmpty) {
                throw ElementNotExistException()
            }
            val heroEntity: HeroEntity = heroEntities.get()
            removeDraft(heroEntity, userId)
            val draftHeroEntity: HeroEntity = fieldsMapper.heroDtoToEntity(dto, userId)

            updateDraftEntity(heroEntity, draftHeroEntity, userId)

            heroEntity.let { heroRepository.save(it) }
            LOGGER.info("Hero with name  = " + heroEntity.name + " was updated with draft version from user = $userId")
        } else {
            dto as WeaponData
            LOGGER.info("Got draft update request from user = $userId for element type of $type with name = " + dto.name)
            val weaponEntities: Optional<WeaponEntity?> = weaponRepository.findById(dto.name)
            if (weaponEntities.isEmpty) {
                throw ElementNotExistException()
            }
            val weaponEntity: WeaponEntity = weaponEntities.get()
            removeDraft(weaponEntity, userId)
            val draftWeaponEntity: WeaponEntity = fieldsMapper.weaponDtoToEntity(dto, userId)

            updateDraftEntity(weaponEntity, draftWeaponEntity, userId)

            weaponEntity.let { weaponRepository.save(it) }
            LOGGER.info("Weapon with name  = " + weaponEntity.name + " was updated with draft version from user = $userId")
        }
    }

    private fun updateDraftEntity(baseEntity: ItemEntity, draftEntity: ItemEntity, userId: Long) {
        if (baseEntity.drafts == null) {
            baseEntity.drafts = arrayListOf()
        }
        baseEntity.drafts?.add(draftEntity)
        draftEntity.userId = userId
    }

    private fun removeDraft(baseEntity: ItemEntity, userId: Long) {
        if (baseEntity.drafts != null) {
            var oldDraftEntity: ItemEntity? = null
            for (draft in baseEntity.drafts!!) {
                if (draft.userId == userId) {
                    oldDraftEntity = draft
                }
            }

            oldDraftEntity?.let {
                when (it) {
                    is WeaponEntity -> removeImages(it)
                    is HeroEntity -> removeImages(it)
                    else -> {}
                }
                baseEntity.drafts?.remove(it)
                if (baseEntity.drafts?.size == 0) {
                    baseEntity.drafts = null
                }
            }
        }
    }

    private fun removeImages(oldHero: HeroEntity) {
        oldHero.mainImage?.let { removeFile(it) }
        oldHero.icon?.let { removeFile(it) }
        oldHero.drafts?.forEach { d -> removeImages(d as HeroEntity) }
    }

    private fun removeImages(oldWeapon: WeaponEntity) {
        oldWeapon.mainImage?.let { removeFile(it) }
        oldWeapon.entireIcon?.let { removeFile(it) }
        oldWeapon.brokenIcon?.let { removeFile(it) }
        oldWeapon.drafts?.forEach { d -> removeImages(d as WeaponEntity) }
    }

    private fun removeFile(filePath: String) {
        LOGGER.info("Remove file $filePath")
        val file = File(filePath)
        if (file.exists()) {
            FileUtils.forceDelete(file)
        }
    }


    private fun renameFileAndGetNewPath(oldPath: String): String {
        val newPath = oldPath.substringBeforeLast("-") + "." + oldPath.substringAfterLast(".")
        val source: Path = Paths.get(oldPath)
        Files.move(source, source.resolveSibling(newPath))
        return newPath
    }

    @Throws(ElementNotExistException::class, ElementAlreadyExistException::class)
    override fun updateElement(elementName: String, type: String, userId: BigDecimal) {
        LOGGER.info("Got update request for element type of $type with name = $elementName")
        if (type == ElementTypes.HERO.value) {
            val heroEntities: Optional<HeroEntity?> = heroRepository.findById(elementName)
            if (heroEntities.isEmpty) {
                throw ElementNotExistException()
            } else {
                removeImages(heroEntities.get())
                val draftHeroes: ArrayList<ItemEntity>? = heroEntities.get().drafts
                if (draftHeroes == null) {
                    throw ElementNotExistException()
                } else {
                    val draftHero = draftHeroes.filter { t -> t.userId == userId.toLong() }[0] as HeroEntity
                    val draftImagePath = draftHero.mainImage
                    val draftIconPath = draftHero.mainImage

                    val entityToUpdate = heroEntities.get()
                    removeImages(entityToUpdate)
                    entityToUpdate.mainImage = draftImagePath?.let { renameFileAndGetNewPath(it) }
                    LOGGER.info(entityToUpdate.mainImage)

                    entityToUpdate.icon = draftIconPath?.let { renameFileAndGetNewPath(it) }
                    LOGGER.info(entityToUpdate.icon)

                    entityToUpdate.let { heroRepository.save(it) }
                    removeDraft(entityToUpdate, userId.toLong())
                }
            }
        } else {
            val weaponEntities: Optional<WeaponEntity?> = weaponRepository.findById(elementName)
            if (weaponEntities.isEmpty) {
                throw ElementNotExistException()
            } else {
                removeImages(weaponEntities.get())
                val draftWeapons: ArrayList<ItemEntity>? = weaponEntities.get().drafts
                if (draftWeapons == null) {
                    throw ElementNotExistException()
                } else {
                    val draftWeapon = draftWeapons.filter { t -> t.userId == userId.toLong() }[0] as WeaponEntity
                    val draftImagePath = draftWeapon.mainImage
                    val draftEntireIconPath = draftWeapon.entireIcon
                    val draftBrokenIconPath = draftWeapon.brokenIcon

                    val entityToUpdate = weaponEntities.get()
                    removeImages(entityToUpdate)
                    entityToUpdate.mainImage = draftImagePath?.let { renameFileAndGetNewPath(it) }
                    entityToUpdate.entireIcon = draftEntireIconPath?.let { renameFileAndGetNewPath(it) }
                    entityToUpdate.brokenIcon = draftBrokenIconPath?.let { renameFileAndGetNewPath(it) }
                    entityToUpdate.let { weaponRepository.save(it) }
                    removeDraft(entityToUpdate, userId.toLong())
                }
            }
        }
    }

    @Throws(ElementNotExistException::class)
    override fun deleteDraftElement(elementName: String, type: String, userId: Long) {
        LOGGER.info("Got draft delete request for element type of $type with name = $elementName")
        if (type == ElementTypes.HERO.value) {
            val heroEntities: Optional<HeroEntity?> = heroRepository.findById(elementName)
            if (heroEntities.isEmpty) {
                throw ElementNotExistException()
            }
            val heroEntity: HeroEntity = heroEntities.get()
            removeDraft(heroEntity, userId)
            heroEntity.let { heroRepository.save(it) }
            LOGGER.info("Draft hero info from user = $userId for element with name  = $elementName was deleted")
        } else {
            val weaponEntities: Optional<WeaponEntity?> = weaponRepository.findById(elementName)
            if (weaponEntities.isEmpty) {
                throw ElementNotExistException()
            }
            val weaponEntity: WeaponEntity = weaponEntities.get()
            removeDraft(weaponEntity, userId)
            weaponEntity.let { weaponRepository.save(it) }
            LOGGER.info("Draft weapon info from user = $userId for element with name  = $elementName was deleted")
        }
    }

    override fun getAllEntities(type: String): List<String> {
        LOGGER.info("Got get request for all elements of type = $type")
        return if (type == ElementTypes.HERO.value) {
            val heroEntities: MutableList<HeroEntity?> = heroRepository.findAll()
            if (heroEntities.isEmpty()) {
                throw ElementNotExistException()
            }
            heroEntities.stream().map { el -> el?.name!! }.toList()
        } else {
            val weaponEntities: MutableList<WeaponEntity?> = weaponRepository.findAll()
            if (weaponEntities.isEmpty()) {
                throw ElementNotExistException()
            }
            weaponEntities.stream().map { el -> el?.name!! }.toList()
        }

    }

    @Throws(ElementNotExistException::class, InvalidNameException::class)
    override fun getDraftElementByName(
        elementName: String,
        type: String,
        userId: Long
    ): AddElementRequestDto<Any>? {
        LOGGER.info("Got get request for draft element of type = $type with id = $elementName from user = $userId")
        if (elementName.isEmpty() || elementName.isBlank()) {
            throw InvalidNameException()
        }

        return if (type == ElementTypes.HERO.value) {
            val heroEntities = heroRepository.findById(elementName)
            if (heroEntities.isEmpty) {
                throw ElementNotExistException()
            }
            val draftHero: ArrayList<ItemEntity>? = heroEntities.get().drafts
            if (draftHero == null) {
                throw ElementNotExistException()
            } else {
                fieldsMapper.heroEntityToDto(draftHero.filter { t -> t.userId == userId }[0] as HeroEntity)
            }

        } else {
            val weaponEntities = weaponRepository.findById(elementName)
            if (weaponEntities.isEmpty) {
                throw ElementNotExistException()
            }
            val draftWeapon = weaponEntities.get().drafts
            if (draftWeapon == null) {
                throw ElementNotExistException()
            } else {
                fieldsMapper.weaponEntityToDto(draftWeapon.filter { t -> t.userId == userId }[0] as WeaponEntity)
            }
        }
    }

    @Throws(ElementNotExistException::class, InvalidNameException::class)
    override fun getElementByName(elementName: String, type: String): AddElementRequestDto<Any>? {
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