package scorewarrior.syncService.entity

import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import javax.validation.constraints.Size

open class ItemEntity(@NotNull @Size(min = 1, max = 100) name: String) : BaseEntity() {

    @Id
    var name: String? = name

    @Size(min = 1)
    var userId: Long? = null


}