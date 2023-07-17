package frontend.pages

import frontend.components.Header
import react.FC
import react.Props
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import react.router.dom.Link

val IndexPage = FC<Props> {
    Header {}
    h1 {
        +"Index Page"
    }

    Link {
        to = "/heroes/soopa_doopa"
        +"Soopa Doopa hero"
    }
}