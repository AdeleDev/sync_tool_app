package common.helpers

abstract class PathBuilder(val parent: PathBuilder? = null, val relativePath: String) {
    fun Path(relPath: String) = object: PathBuilder(this, relPath) {
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (parent != null) {
            sb.append(parent)
        }
        sb.append(relativePath)
        return sb.toString().replace("/+".toRegex(), "/")
    }
}
