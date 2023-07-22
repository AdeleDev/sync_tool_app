package scorewarrior.syncService.api.dto

import org.springframework.web.multipart.MultipartFile

data class HeroData(val name: String, val mainImage: MultipartFile?, val icon: MultipartFile?) {

}
