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


    private fun storeImageFile(heroDto: HeroData, userId: Long?): String? {
        val filePath = heroFolder + "/" + heroDto.mainImage?.name + "-" + heroDto.name + (userId ?: "");
        if (heroDto.mainImage != null) {
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
        val filePath = heroFolder + "/" + heroDto.icon?.name + "-" + heroDto.name + (userId ?: "")
        if (heroDto.icon != null) {
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


    fun storeImageFile(weaponDto: WeaponData, userId: Long?): String? {
        val filePath = heroFolder + "/" + weaponDto.mainImage?.name + "-" + weaponDto.name + (userId ?: "")
        val file = File(filePath)
        if (weaponDto.mainImage != null) {
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
        val filePath = heroFolder + "/" + weaponDto.entireIcon?.name + "-" + weaponDto.name + (userId ?: "")
        val file = File(filePath)
        if (weaponDto.entireIcon != null) {
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
        val filePath = heroFolder + "/" + weaponDto.brokenIcon?.name + "-" + weaponDto.name + (userId ?: "")
        val file = File(filePath)
        if (weaponDto.brokenIcon != null) {
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