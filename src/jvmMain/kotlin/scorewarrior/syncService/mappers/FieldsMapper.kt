package scorewarrior.syncService.mappers

import org.apache.tomcat.util.http.fileupload.FileUtils
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.stereotype.Component
import scorewarrior.syncService.LOGGER
import scorewarrior.syncService.api.dto.HeroData
import scorewarrior.syncService.api.dto.WeaponData
import scorewarrior.syncService.entity.HeroEntity
import scorewarrior.syncService.entity.WeaponEntity
import scorewarrior.syncservice.model.UpdateElementRequestDto
import java.io.File
import java.io.FileOutputStream

@Component
class FieldsMapper() {

    @Value("\${imageFolder.path}")
    private val imageFolder: String = "./data"

    private val heroFolder: String = "$imageFolder/hero"
    private val weaponFolder: String = "$imageFolder/weapon"

    fun heroDtoToEntity(heroDto: HeroData, userId: Long? = null): HeroEntity {

        return HeroEntity(heroDto.name, storeImageFile(heroDto, userId), storeIconFile(heroDto, userId))
    }

    fun weaponDtoToEntity(weaponDto: WeaponData, userId: Long? = null): WeaponEntity {
        return WeaponEntity(
            weaponDto.name,
            storeImageFile(weaponDto, userId),
            storeEntireIconFile(weaponDto, userId),
            storeBrokenIconFile(weaponDto, userId)
        )
    }

    fun heroEntityToDto(heroEntity: HeroEntity): UpdateElementRequestDto<Any> {
        return heroEntity.name?.let { it ->
            val form = UpdateElementRequestDto<Any>()
            form.add("name", it)
            heroEntity.mainImage?.let { form.add("mainImage", (FileSystemResource(it))) }
            heroEntity.icon?.let { form.add("icon", (FileSystemResource(it))) }
            form
        }!!

    }

    fun weaponEntityToDto(weaponEntity: WeaponEntity): UpdateElementRequestDto<Any> {
        return weaponEntity.name?.let { it ->
            val form = UpdateElementRequestDto<Any>()
            form.add("name", it)
            weaponEntity.mainImage?.let { form.add("mainImage", (FileSystemResource(it))) }
            weaponEntity.entireIcon?.let { form.add("entireIcon", (FileSystemResource(it))) }
            weaponEntity.brokenIcon?.let { form.add("brokenIcon", (FileSystemResource(it))) }
            form
        }!!
    }

    private fun buildHeroFilePath(imageName: String, elementName: String, userId: String, original: String): String {
        return "$heroFolder/$imageName-$elementName-$userId-$original"
    }


    private fun storeImageFile(heroDto: HeroData, userId: Long?): String? {
        if (heroDto.mainImage != null) {
            val filePath = buildHeroFilePath(heroDto.mainImage.name,heroDto.name, userId.toString(),
                heroDto.mainImage.originalFilename!!
            )
            val file = File(filePath)
            FileUtils.forceMkdir(File(heroFolder))
            file.createNewFile()
            FileOutputStream(file).use { fos ->
                IOUtils.copy(heroDto.mainImage.inputStream, fos)
            }
            LOGGER.info("Save file " + filePath)
            return filePath
        }
        return null
    }

    private fun storeIconFile(heroDto: HeroData, userId: Long?): String? {
        if (heroDto.icon != null) {
            val filePath = buildHeroFilePath(heroDto.icon.name,heroDto.name, userId.toString(), heroDto.icon.originalFilename!!)
            val file = File(filePath)

            FileUtils.forceMkdir(File(heroFolder))
            file.createNewFile()
            FileOutputStream(file).use { fos ->
                IOUtils.copy(heroDto.icon.inputStream, fos)
            }
            LOGGER.info("Save file " + filePath)
            return filePath
        }
        return null

    }


    private fun buildWeaponFilePath(imageName: String, elementName: String, userId: String, original: String): String {
        return "$weaponFolder/$imageName-$elementName-$userId-$original"
    }

    fun storeImageFile(weaponDto: WeaponData, userId: Long?): String? {
        if (weaponDto.mainImage != null) {
            val filePath = buildWeaponFilePath(weaponDto.mainImage.name, weaponDto.name, userId.toString(), weaponDto.mainImage.originalFilename!!)
            val file = File(filePath)
            FileUtils.forceMkdir(File(weaponFolder))
            file.createNewFile()
            FileOutputStream(file).use { fos ->
                IOUtils.copy(weaponDto.mainImage.inputStream, fos)
            }
            LOGGER.info("Save file " + filePath)
            return filePath
        }
        return null
    }

    fun storeEntireIconFile(weaponDto: WeaponData, userId: Long?): String? {
        if (weaponDto.entireIcon != null) {
            val filePath = buildWeaponFilePath(weaponDto.entireIcon.name, weaponDto.name, userId.toString(), weaponDto.entireIcon.originalFilename!!)
            val file = File(filePath)
            FileUtils.forceMkdir(File(weaponFolder))
            file.createNewFile()
            FileOutputStream(file).use { fos ->
                IOUtils.copy(weaponDto.entireIcon.inputStream, fos)
            }
            LOGGER.info("Save file " + filePath)
            return filePath
        }
        return null
    }

    fun storeBrokenIconFile(weaponDto: WeaponData, userId: Long?): String? {
        if (weaponDto.brokenIcon != null) {
            val filePath = buildWeaponFilePath(weaponDto.brokenIcon.name, weaponDto.name, userId.toString(), weaponDto.brokenIcon.originalFilename!!)
            val file = File(filePath)
            FileUtils.forceMkdir(File(weaponFolder))
            file.createNewFile()
            FileOutputStream(file).use { fos ->
                IOUtils.copy(weaponDto.brokenIcon.inputStream, fos)
            }
            LOGGER.info("Save file " + filePath)
            return filePath
        }
        return null
    }

}