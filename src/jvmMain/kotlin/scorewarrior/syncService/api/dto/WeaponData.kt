package scorewarrior.syncService.api.dto

import org.springframework.web.multipart.MultipartFile

data class WeaponData(val name: String, val mainImage: MultipartFile?, val entireIcon: MultipartFile?, val brokenIcon: MultipartFile?) {

}
