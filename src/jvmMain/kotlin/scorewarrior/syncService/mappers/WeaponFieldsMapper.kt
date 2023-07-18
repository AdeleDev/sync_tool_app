package scorewarrior.syncService.mappers

import org.apache.tomcat.util.http.fileupload.IOUtils
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers
import scorewarrior.syncService.entity.WeaponEntity
import scorewarrior.syncservice.model.GetAllElements200ResponseInnerDto
import scorewarrior.syncservice.model.WeaponDto
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

@Mapper
interface WeaponFieldsMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = ".", target = "mainImage", qualifiedByName = ["storeImageFile"])
    @Mapping(source = ".", target = "entireIcon", qualifiedByName = ["storeEntireIconFile"])
    @Mapping(source = ".", target = "brokenIcon", qualifiedByName = ["storeBrokenIconFile"])

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "drafts", ignore = true)
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "lastModifiedTimestamp", ignore = true)
    fun dtoToEntity(weapon: WeaponDto): WeaponEntity

    @InheritInverseConfiguration
    @Mapping(
        target = "mainImage",
        expression = "java(new org.springframework.core.io.InputStreamResource(new java.io.FileInputStream(weapon.getMainImage())))"
    )
    @Mapping(
        target = "entireIcon",
        expression = "java(new org.springframework.core.io.InputStreamResource(new java.io.FileInputStream(weapon.getEntireIcon())))"
    )
    @Mapping(
        target = "brokenIcon",
        expression = "java(new org.springframework.core.io.InputStreamResource(new java.io.FileInputStream(weapon.getBrokenIcon())))"
    )
    @Mapping(target = "icon", ignore = true)
    @Throws(FileNotFoundException::class)
    fun entityToDto(weapon: WeaponEntity?): GetAllElements200ResponseInnerDto?

    @Named("storeImageFile")
    fun storeImageFile(weaponDto: WeaponDto): String? {
        val file = File("\${imageFolder.path}" + "/weapon/image-" + weaponDto.name)
        val fos = FileOutputStream(file)
        IOUtils.copy(weaponDto.mainImage?.inputStream, fos)
        return file.name
    }

    @Named("storeEntireIconFile")
    fun storeEntireIconFile(weaponDto: WeaponDto): String? {
        val file = File("\${imageFolder.path}" + "/weapon/entireIcon-" + weaponDto.name)
        val fos = FileOutputStream(file)
        IOUtils.copy(weaponDto.entireIcon?.inputStream, fos)
        return file.name
    }

    @Named("storeBrokenIconFile")
    fun storeBrokenIconFile(weaponDto: WeaponDto): String? {
        val file = File("\${imageFolder.path}" + "/weapon/brokenIcon-" + weaponDto.name)
        val fos = FileOutputStream(file)
        IOUtils.copy(weaponDto.brokenIcon?.inputStream, fos)
        return file.name
    }


    companion object {
        val WEAPON_FIELDS_MAPPER: WeaponFieldsMapper =
            Mappers.getMapper(WeaponFieldsMapper::class.java)
    }
}