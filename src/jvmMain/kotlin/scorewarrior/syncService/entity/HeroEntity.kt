package scorewarrior.syncService.entity

import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Size

@Document("heroes")
class HeroEntity(
    name: String,
    @Size(min = 1, max = 200) var mainImage: String?,
    @Size(min = 1, max = 200) var icon: String?,
) : ItemEntity(name) {

    lateinit var drafts: ArrayList<HeroEntity>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HeroEntity

        if (mainImage != other.mainImage) return false
        if (icon != other.icon) return false
        if (name != other.name) return false
        if (userId != other.userId) return false
        if (drafts != other.drafts) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mainImage?.hashCode() ?: 0
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (userId?.hashCode() ?: 0)
        result = 31 * result + drafts.hashCode()
        return result
    }


}