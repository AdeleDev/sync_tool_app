package components

import csstype.ClassName
import mui.material.Toolbar
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span

typealias FooterProps = Props

val Footer = FC<FooterProps> {
    div {
        Toolbar {
            className = ClassName("footer")
            span {
                className = ClassName("text-muted")
                +"All Rights Reserved 2023 @Adele"
            }
        }

    }
}