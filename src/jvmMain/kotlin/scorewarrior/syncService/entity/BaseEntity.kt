package scorewarrior.syncService.entity

import java.time.OffsetDateTime

abstract class BaseEntity {
    var createdTimestamp: OffsetDateTime? = null
    var lastModifiedTimestamp: OffsetDateTime? = null
}