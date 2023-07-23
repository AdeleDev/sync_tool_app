package components.tables.existingitems

import components.NewItemFields
import csstype.px
import emotion.react.css
import kotlinx.js.Object
import react.FC
import react.Props
import react.StateSetter
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2

external interface ItemListProps : Props {
    var heroItemList: Array<Object>
    var heroListSetter: StateSetter<Array<Object>>
    var weaponItemList: Array<Object>
    var weaponListSetter: StateSetter<Array<Object>>
    var userName: String
    var elementCounter: StateSetter<Int>
}


val ItemList = FC<ItemListProps> { props ->
    div {

        css {
            maxHeight = 800.px
            margin = 50.px
        }
        h2 { +"Heroes " }
        div {
            ColumnsHero {
                heroColumnItemList = props.heroItemList
                userName = props.userName
            }
        }

        if (props.userName == "designer") {
            div {
                NewItemFields {
                    columnItemList = props.heroItemList
                    columnListSetter = props.heroListSetter
                    type = "hero"
                    elementCounter = props.elementCounter
                }
            }
        }

    }
    div {

        css {
            maxHeight = 800.px
            margin = 50.px
        }
        h2 { +"Weapons" }
        div {
            ColumnsWeapon {
                weaponColumnItemList = props.weaponItemList
                userName = props.userName
            }

        }
        if (props.userName == "designer") {
            div {
                NewItemFields {
                    columnItemList = props.weaponItemList
                    columnListSetter = props.weaponListSetter
                    type = "weapon"
                    elementCounter = props.elementCounter
                }
            }
        }

    }

}

