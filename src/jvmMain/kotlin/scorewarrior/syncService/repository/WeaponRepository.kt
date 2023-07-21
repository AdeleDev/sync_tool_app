package scorewarrior.syncService.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import scorewarrior.syncService.entity.WeaponEntity

@Repository
interface WeaponRepository : MongoRepository<WeaponEntity?, String?> {
}