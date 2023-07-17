package common

import common.helpers.PathBuilder

// Type-safe paths

class RootPaths: PathBuilder(null, "/") {
    val Heroes = HeroesPaths(this)
    val Weapons = WeaponsPaths(this)
    val Commit = Path("/commit")
    val Api = ApiPaths(this)
}

class HeroesPaths(parent: PathBuilder): PathBuilder(parent, "/heroes") {
    val New = Path("/new")
    fun Id(id: String) = Path("/${id}")
}

class WeaponsPaths(parent: PathBuilder): PathBuilder(parent, "/weapons") {
    val New = Path("/new")
    fun Id(id: String) = Path("/${id}")
}

class ApiPaths(parent: PathBuilder): PathBuilder(parent, "/api") {
    val V1 = ApiV1Paths(this)
}

class ApiV1Paths(parent: PathBuilder): PathBuilder(parent, "/v1") {
    val Heroes =
        ApiHeroesPath(this)
}

class ApiHeroesPath(parent: PathBuilder): PathBuilder(parent, "/heroes") {
    fun Id(id: String) = Path("/${id}")
}
