package components

import csstype.ClassName
import csstype.px
import emotion.react.css
import kotlinx.js.Object
import mui.material.Input
import mui.material.InputBaseColor
import newItemPostRequest
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.StateSetter
import react.dom.events.ChangeEvent
import react.dom.events.MouseEvent
import react.dom.html.ReactHTML.div
import react.useState

external interface ItemProps : Props {
    var columnItemList: Array<Object>
    var columnListSetter: StateSetter<Array<Object>>
    var type: String
    var elementCounter: StateSetter<Int>
}

val NewItemFields = FC<ItemProps> { props ->

    val (name, setName) = useState("")

    val handleClick = { event: MouseEvent<*, *> ->
        event.preventDefault()
        newItemPostRequest(name, props.type, props.elementCounter, props.columnItemList.size)
    }

    val handleChange = { event: ChangeEvent<*> ->
        event.preventDefault()
        val target = event.target as HTMLInputElement
        setName(target.value)
    }
    div {
        css {
            margin = 50.px
        }

        className = ClassName("list-group-item-input")
        Input {
            type = "text"
            placeholder = "Enter object name"
            color = InputBaseColor.warning
            onChange = handleChange
        }
        ButtonStyled {
            action = handleClick
            buttonText = "Submit"
        }
    }
}
