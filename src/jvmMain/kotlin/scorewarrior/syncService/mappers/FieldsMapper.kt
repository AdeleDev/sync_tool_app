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
import scorewarrior.syncservice.model.AddElementRequestDto
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

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

    fun heroEntityToDto(heroEntity: HeroEntity): AddElementRequestDto<Any> {
        return heroEntity.name?.let { it ->
            val form = AddElementRequestDto<Any>()
            form.add("name", it)
            heroEntity.mainImage?.let { form.add("mainImage", (FileSystemResource(it))) }
            heroEntity.icon?.let { form.add("icon", (FileSystemResource(it))) }
            form
        }!!

    }

    fun weaponEntityToDto(weaponEntity: WeaponEntity): AddElementRequestDto<Any> {
        return weaponEntity.name?.let { it ->
            val form = AddElementRequestDto<Any>()
            form.add("name", it)
            weaponEntity.mainImage?.let { form.add("mainImage", (FileSystemResource(it))) }
            weaponEntity.entireIcon?.let { form.add("entireIcon", (FileSystemResource(it))) }
            weaponEntity.brokenIcon?.let { form.add("brokenIcon", (FileSystemResource(it))) }
            form
        }!!
    }


    private fun createImageFile(filePath: String, inputStream: InputStream) {
        val file = File(filePath)
        FileUtils.forceMkdir(File(weaponFolder))
        file.createNewFile()
        FileOutputStream(file).use { fos ->
            IOUtils.copy(inputStream, fos)
        }
        LOGGER.info("Save file $filePath")
    }


    private fun buildHeroFilePath(imageName: String, elementName: String, userId: String, original: String): String {
        return "$heroFolder/$imageName-$elementName-$userId-$original"
    }


    private fun storeImageFile(heroDto: HeroData, userId: Long?): String? {
        if (heroDto.mainImage != null) {
            val filePath = buildHeroFilePath(
                heroDto.mainImage.name, heroDto.name, userId.toString(),
                heroDto.mainImage.originalFilename!!
            )
            createImageFile(filePath, heroDto.mainImage.inputStream)
            return filePath
        }
        return null
    }

    private fun storeIconFile(heroDto: HeroData, userId: Long?): String? {
        if (heroDto.icon != null) {
            val filePath =
                buildHeroFilePath(heroDto.icon.name, heroDto.name, userId.toString(), heroDto.icon.originalFilename!!)
            createImageFile(filePath, heroDto.icon.inputStream)
            return filePath
        }
        return null
    }


    private fun buildWeaponFilePath(imageName: String, elementName: String, userId: String, original: String): String {
        return "$weaponFolder/$imageName-$elementName-$userId-$original"
    }

    fun storeImageFile(weaponDto: WeaponData, userId: Long?): String? {
        if (weaponDto.mainImage != null) {
            val filePath = buildWeaponFilePath(
                weaponDto.mainImage.name,
                weaponDto.name,
                userId.toString(),
                weaponDto.mainImage.originalFilename!!
            )
            createImageFile(filePath, weaponDto.mainImage.inputStream)
            return filePath
        }
        return null
    }

    fun storeEntireIconFile(weaponDto: WeaponData, userId: Long?): String? {
        if (weaponDto.entireIcon != null) {
            val filePath = buildWeaponFilePath(
                weaponDto.entireIcon.name,
                weaponDto.name,
                userId.toString(),
                weaponDto.entireIcon.originalFilename!!
            )
            createImageFile(filePath, weaponDto.entireIcon.inputStream)
            return filePath
        }
        return null
    }

    fun storeBrokenIconFile(weaponDto: WeaponData, userId: Long?): String? {
        if (weaponDto.brokenIcon != null) {
            val filePath = buildWeaponFilePath(
                weaponDto.brokenIcon.name,
                weaponDto.name,
                userId.toString(),
                weaponDto.brokenIcon.originalFilename!!
            )
            createImageFile(filePath, weaponDto.brokenIcon.inputStream)
            return filePath
        }
        return null
    }

}