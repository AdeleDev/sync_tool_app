package components

import csstype.Color
import csstype.Padding
import csstype.px
import mui.material.Button
import mui.system.sx
import org.w3c.dom.HTMLButtonElement
import react.FC
import react.Props
import react.dom.events.MouseEventHandler
import react.dom.html.ButtonType

external interface ButtonProps : Props {
    var action: MouseEventHandler<HTMLButtonElement>?
    var buttonText: String
    var disabled: Boolean
}

val ButtonStyled = FC<ButtonProps> { props ->
    Button {
        type = ButtonType.submit

        sx {
            hover {
                backgroundColor = Color("#021721")
            }
            backgroundColor = Color("#12445e")
            padding = Padding(12.px, 26.px)
            color = Color("#fff")
            marginLeft = 20.px
        }
        +props.buttonText

        onClick = props.action
        disabled = props.disabled

    }
}

