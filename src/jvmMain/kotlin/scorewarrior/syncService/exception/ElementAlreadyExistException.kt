package scorewarrior.syncService.exception

class ElementAlreadyExistException(private val name: String) : Exception() {
    override val message: String
        get() = "Element with name $name already exists"
}