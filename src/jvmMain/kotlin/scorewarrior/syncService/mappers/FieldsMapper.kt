package scorewarrior.syncService.mappers

import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.core.io.InputStreamResource
import org.springframework.stereotype.Component
import scorewarrior.syncService.entity.HeroEntity
import scorewarrior.syncService.entity.WeaponEntity
import scorewarrior.syncservice.model.GetAllElements200ResponseInnerDto
import scorewarrior.syncservice.model.HeroDto
import scorewarrior.syncservice.model.WeaponDto
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Component
class FieldsMapper() {

    fun heroDtoToEntity(heroDto: HeroDto): HeroEntity {
        return HeroEntity(heroDto.name, storeImageFile(heroDto), storeIconFile(heroDto))
    }

    fun weaponDtoToEntity(weaponDto: WeaponDto): WeaponEntity {
        return WeaponEntity(
            weaponDto.name,
            storeImageFile(weaponDto),
            storeEntireIconFile(weaponDto),
            storeBrokenIconFile(weaponDto)
        )
    }

    fun heroEntityToDto(heroEntity: HeroEntity): GetAllElements200ResponseInnerDto {
        return heroEntity.name?.let { it ->
            GetAllElements200ResponseInnerDto(
                it,
                heroEntity.mainImage?.let { FileInputStream(it) }?.let { InputStreamResource(it) },
                heroEntity.icon?.let { FileInputStream(it) }?.let { InputStreamResource(it) })
        }!!

    }

    fun weaponEntityToDto(weaponEntity: WeaponEntity): GetAllElements200ResponseInnerDto {
        return weaponEntity.name?.let { it ->
            GetAllElements200ResponseInnerDto(
                it,
                weaponEntity.mainImage?.let { FileInputStream(it) }?.let { InputStreamResource(it) },
                weaponEntity.entireIcon?.let { FileInputStream(it) }?.let { InputStreamResource(it) },
                weaponEntity.brokenIcon?.let { FileInputStream(it) }?.let { InputStreamResource(it) })
        }!!
    }

    //    @Mapping(source = "name", target = "name")
//    @Mapping(source = ".", target = "mainImage", qualifiedByName = ["storeImageFile"])
//    @Mapping(source = ".", target = "icon", qualifiedByName = ["storeIconFile"])
//    @Mapping(target = "userId", ignore = true)
//    @Mapping(target = "drafts", ignore = true)
//    @Mapping(target = "createdTimestamp", ignore = true)
//    @Mapping(target = "lastModifiedTimestamp", ignore = true)
//    fun dtoToEntity(heroDto: HeroDto): HeroEntity
//
//    @InheritInverseConfiguration
//    @Mapping(
//        target = "mainImage",
//        expression = "java(new org.springframework.core.io.InputStreamResource(new java.io.FileInputStream(hero.getMainImage())))"
//    )
//    @Mapping(
//        target = "icon",
//        expression = "java(new org.springframework.core.io.InputStreamResource(new java.io.FileInputStream(hero.getIcon())))"
//    )
//    @Mapping(target = "entireIcon", ignore = true)
//    @Mapping(target = "brokenIcon", ignore = true)
//    @Throws(FileNotFoundException::class)
//    fun entityToDto(hero: HeroEntity?): GetAllElements200ResponseInnerDto?
//
    private fun storeImageFile(heroDto: HeroDto): String? {
        val file = File("\${imageFolder.path}" + "/hero/image-" + heroDto.name)
        val fos = FileOutputStream(file)
        IOUtils.copy(heroDto.mainImage?.inputStream, fos)
        return file.name
    }

    private fun storeIconFile(heroDto: HeroDto): String? {
        val file = File("\${imageFolder.path}" + "/hero/icon-" + heroDto.name)
        val fos = FileOutputStream(file)
        IOUtils.copy(heroDto.icon?.inputStream, fos)
        return file.name
    }


    fun storeImageFile(weaponDto: WeaponDto): String? {
        val file = File("\${imageFolder.path}" + "/weapon/image-" + weaponDto.name)
        val fos = FileOutputStream(file)
        IOUtils.copy(weaponDto.mainImage?.inputStream, fos)
        return file.name
    }

    fun storeEntireIconFile(weaponDto: WeaponDto): String? {
        val file = File("\${imageFolder.path}" + "/weapon/entireIcon-" + weaponDto.name)
        val fos = FileOutputStream(file)
        IOUtils.copy(weaponDto.entireIcon?.inputStream, fos)
        return file.name
    }

    fun storeBrokenIconFile(weaponDto: WeaponDto): String? {
        val file = File("\${imageFolder.path}" + "/weapon/brokenIcon-" + weaponDto.name)
        val fos = FileOutputStream(file)
        IOUtils.copy(weaponDto.brokenIcon?.inputStream, fos)
        return file.name
    }

}