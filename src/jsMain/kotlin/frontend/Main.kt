package frontend

import App
import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {
    val rootDiv = document.getElementById("root") ?: error("Couldn't find root node")
    createRoot(rootDiv).render(App.create())
}