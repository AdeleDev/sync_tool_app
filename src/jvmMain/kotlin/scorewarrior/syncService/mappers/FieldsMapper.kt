package scorewarrior.syncService.mappers

import org.apache.tomcat.util.http.fileupload.FileUtils
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import scorewarrior.syncService.api.dto.HeroData
import scorewarrior.syncService.api.dto.WeaponData
import scorewarrior.syncService.entity.HeroEntity
import scorewarrior.syncService.entity.WeaponEntity
import scorewarrior.syncservice.model.GetAllElements200ResponseInnerDto
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

    fun weaponDtoToEntity(weaponDto: WeaponData): WeaponEntity {
        return WeaponEntity(
            weaponDto.name,
            storeImageFile(weaponDto),
            storeEntireIconFile(weaponDto),
            storeBrokenIconFile(weaponDto)
        )
    }

    fun heroEntityToDto(heroEntity: HeroEntity): GetAllElements200ResponseInnerDto {
        return heroEntity.name?.let { it ->
            HeroData(
                it, null, null
            )
//                heroEntity.mainImage?.let { FileInputStream(it) }?.let { InputStreamResource(it) },
//                heroEntity.icon?.let { FileInputStream(it) }?.let { InputStreamResource(it) })
        }!!

    }

    fun weaponEntityToDto(weaponEntity: WeaponEntity): GetAllElements200ResponseInnerDto {
        return weaponEntity.name?.let { it ->
            WeaponData(
                it,
                null, null, null
            )
//                weaponEntity.mainImage?.let { FileInputStream(it) }?.let { InputStreamResource(it) },
//                weaponEntity.entireIcon?.let { FileInputStream(it) }?.let { InputStreamResource(it) },
//                weaponEntity.brokenIcon?.let { FileInputStream(it) }?.let { InputStreamResource(it) })
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
            return filePath
        }
        return null

    }


    fun storeImageFile(weaponDto: WeaponData): String? {
        val file = File(weaponFolder + "/image-" + weaponDto.name)
        if (weaponDto.mainImage != null) {
            FileUtils.forceMkdir(File(weaponFolder))
            file.createNewFile()
            FileOutputStream(file).use { fos ->
                IOUtils.copy(weaponDto.mainImage.inputStream, fos)
            }
            return file.name
        }
        return null
    }

    fun storeEntireIconFile(weaponDto: WeaponData): String? {
        val file = File(weaponFolder + "/entireIcon-" + weaponDto.name)
        if (weaponDto.entireIcon != null) {
            FileUtils.forceMkdir(File(weaponFolder))
            file.createNewFile()
            FileOutputStream(file).use { fos ->
                IOUtils.copy(weaponDto.entireIcon.inputStream, fos)
            }
            return file.name
        }
        return null
    }

    fun storeBrokenIconFile(weaponDto: WeaponData): String? {
        val file = File(weaponFolder + "/brokenIcon-" + weaponDto.name)
        if (weaponDto.brokenIcon != null) {
            FileUtils.forceMkdir(File(weaponFolder))
            file.createNewFile()
            FileOutputStream(file).use { fos ->
                IOUtils.copy(weaponDto.brokenIcon.inputStream, fos)
            }
            return file.name
        }
        return null
    }

}