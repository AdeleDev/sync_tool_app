package scorewarrior.syncService.mappers

import org.apache.tomcat.util.http.fileupload.IOUtils
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.factory.Mappers
import scorewarrior.syncService.entity.HeroEntity
import scorewarrior.syncservice.model.GetAllElements200ResponseInnerDto
import scorewarrior.syncservice.model.HeroDto
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream


@Mapper
interface HeroFieldsMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = ".", target = "mainImage", qualifiedByName = ["storeImageFile"])
    @Mapping(source = ".", target = "icon", qualifiedByName = ["storeIconFile"])
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "drafts", ignore = true)
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "lastModifiedTimestamp", ignore = true)
    fun dtoToEntity(heroDto: HeroDto): HeroEntity

    @InheritInverseConfiguration
    @Mapping(
        target = "mainImage",
        expression = "java(new org.springframework.core.io.InputStreamResource(new java.io.FileInputStream(hero.getMainImage())))"
    )
    @Mapping(
        target = "icon",
        expression = "java(new org.springframework.core.io.InputStreamResource(new java.io.FileInputStream(hero.getIcon())))"
    )
    @Mapping(target = "entireIcon", ignore = true)
    @Mapping(target = "brokenIcon", ignore = true)
    @Throws(FileNotFoundException::class)
    fun entityToDto(hero: HeroEntity?): GetAllElements200ResponseInnerDto?

    @Named("storeImageFile")
    fun storeImageFile(heroDto: HeroDto): String? {
        val file = File("\${imageFolder.path}" + "/hero/image-" + heroDto.name)
        val fos = FileOutputStream(file)
        IOUtils.copy(heroDto.mainImage?.inputStream, fos)
        return file.name
    }

    @Named("storeIconFile")
    fun storeIconFile(heroDto: HeroDto): String? {
        val file = File("\${imageFolder.path}" + "/hero/icon-" + heroDto.name)
        val fos = FileOutputStream(file)
        IOUtils.copy(heroDto.icon?.inputStream, fos)
        return file.name
    }

    companion object {
        val HERO_FIELDS_MAPPER: HeroFieldsMapper = Mappers.getMapper(HeroFieldsMapper::class.java)
    }
}