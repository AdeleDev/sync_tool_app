package components

import csstype.Color
import csstype.Padding
import csstype.px
import emotion.react.css
import mui.material.Toolbar
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.header

typealias HeaderProps = Props

val Header = FC<HeaderProps> {

    div {
        header {
            Toolbar {
                css {
                    padding = Padding(15.px, 15.px)
                    backgroundColor = Color("#12445e")
                    color = Color("#fff")
                    borderRadius = 4.px
                }
                h1 { +"Sync Service" }
            }
        }
    }
}