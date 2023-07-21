package scorewarrior.syncService.entity

import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Size

@Document("weapons")
class WeaponEntity(
    name: String,
    @Size(min = 1, max = 200) var mainImage: String?,
    @Size(min = 1, max = 200) var entireIcon: String?,
    @Size(min = 1, max = 200) var brokenIcon: String?,
) : ItemEntity(name) {

    lateinit var drafts: ArrayList<WeaponEntity>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeaponEntity

        if (entireIcon != other.entireIcon) return false
        if (brokenIcon != other.brokenIcon) return false
        if (name != other.name) return false
        if (userId != other.userId) return false
        if (drafts != other.drafts) return false

        return true
    }

    override fun hashCode(): Int {
        var result = entireIcon?.hashCode() ?: 0
        result = 31 * result + (brokenIcon?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (userId?.hashCode() ?: 0)
        result = 31 * result + drafts.hashCode()
        return result
    }


}