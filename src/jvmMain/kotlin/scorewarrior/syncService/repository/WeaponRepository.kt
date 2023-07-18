package scorewarrior.syncService.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import scorewarrior.syncService.entity.WeaponEntity

@Repository
interface WeaponRepository : MongoRepository<WeaponEntity?, String?> {

    @Query(value = "{name:'?0', drafts.userId: '?1'}")
    fun findDraftByNameAndUser(name: String, userId: Long): WeaponEntity
}