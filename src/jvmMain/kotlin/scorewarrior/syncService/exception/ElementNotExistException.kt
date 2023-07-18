package scorewarrior.syncService.exception

class ElementNotExistException : Exception() {
    override val message: String
        get() = "No required element exists in repository"
}