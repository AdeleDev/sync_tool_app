package scorewarrior.syncService.exception

class InvalidNameException : Exception() {
    override val message: String
        get() = "Invalid entity name supplied"
}