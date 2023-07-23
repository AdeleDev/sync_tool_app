package components.tables.localchanges

import csstype.px
import emotion.react.css
import kotlinx.js.Object
import react.FC
import react.Props
import react.StateSetter
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2

external interface DraftItemListProps : Props {
    var heroItemList: Array<Object>
    var weaponItemList: Array<Object>
    var userName: String
    var refreshImage: StateSetter<Int>
}


val DraftItemList = FC<DraftItemListProps> { props ->
    div {

        css {
            maxHeight = 800.px
            margin = 50.px
        }
        h2 { +"Draft Heroes " }
        div {
            DraftColumnsHero {
                draftHeroColumnItemList = props.heroItemList
                userName = props.userName
                refreshImage = props.refreshImage
            }
        }
    }
    div {

        css {
            maxHeight = 800.px
            margin = 50.px
        }
        h2 { +"Draft Weapons" }
        div {
            DraftColumnsWeapon {
                draftWeaponColumnItemList = props.weaponItemList
                userName = props.userName
                refreshImage = props.refreshImage
            }

        }
    }

}


