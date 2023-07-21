package scorewarrior.syncService.api.dto

import org.springframework.web.multipart.MultipartFile
import scorewarrior.syncservice.model.GetAllElements200ResponseInnerDto

data class WeaponData(val name: String, val mainImage: MultipartFile?, val entireIcon: MultipartFile?, val brokenIcon: MultipartFile?) : GetAllElements200ResponseInnerDto {

}
