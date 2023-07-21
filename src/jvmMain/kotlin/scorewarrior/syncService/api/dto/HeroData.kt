package scorewarrior.syncService.api.dto

import org.springframework.web.multipart.MultipartFile
import scorewarrior.syncservice.model.GetAllElements200ResponseInnerDto

data class HeroData(val name: String, val mainImage: MultipartFile?, val icon: MultipartFile?) : GetAllElements200ResponseInnerDto {

}
