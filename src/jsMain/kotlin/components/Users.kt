package components

import csstype.Position.Companion.absolute
import csstype.px
import emotion.react.css
import mui.material.Menu
import mui.material.MenuItem
import org.w3c.dom.Element
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLLIElement
import react.FC
import react.Props
import react.StateSetter
import react.dom.events.MouseEvent
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.useState


external interface UserProps : Props {
    var userName: String
    var userSetter: StateSetter<String>
    var refresh: StateSetter<Int>
}


val User = FC<UserProps> { props ->

    var element by useState<Element>()

    val handleContextMenu = { event: MouseEvent<HTMLButtonElement, *> ->
        event.preventDefault()
        element = event.currentTarget
    }

    val handleClose = { event: MouseEvent<*, *> ->
        val target = event.target as HTMLLIElement
        props.userSetter.invoke(target.innerText)
        props.refresh((1..100).random())
        element = null
    }

    div {

        h3 { +"User: ${props.userName}" }
        ButtonStyled {
            action = handleContextMenu
            buttonText = "Switch user"
        }
        Menu {
            css {
                position = absolute
                top = 8.px
                left = 45.px
                fontSize = 18.px
            }
            open = element != null
            anchorEl = { e -> element ?: e }
            onClose = handleClose
            onClick = { element = null }

            MenuItem {
                onClick = handleClose
                +"viewer"
            }
            MenuItem {
                onClick = handleClose
                +"designer"
            }
            MenuItem {
                onClick = handleClose
                +"drawer"
            }
            MenuItem {
                onClick = handleClose
                +"drawer2"
            }
        }
    }

}
