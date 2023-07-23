package components.tables.localchanges

import components.ButtonStyled
import components.tables.HeroColumnHeader
import components.tables.WeaponColumnHeader
import csstype.Gap
import csstype.px
import deleteDraftRequest
import emotion.react.css
import mui.material.*
import mui.system.sx
import org.w3c.files.File
import react.FC
import react.Props
import react.StateSetter
import react.dom.aria.ariaLabel
import react.dom.events.MouseEvent
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import submitDraftRequest

external interface DraftColumnsItemProps : Props {
    var draftHeroColumnItemList: Array<dynamic>
    var draftWeaponColumnItemList: Array<dynamic>
    var userName: String
    var type: String
    var draftItemName: String
    var mainImage: File?
    var icon: File?
    var entireIcon: File?
    var brokenIcon: File?
    var refreshImage: StateSetter<Int>
}


val DraftColumnsHero = FC<DraftColumnsItemProps> { props ->
    val currentUser = props.userName

    div {
        TableContainer {
            Table {
                ariaLabel = "simple-table"
                HeroColumnHeader()
                TableBody {
                    for (i in 0 until props.draftHeroColumnItemList.size) {
                        val name = props.draftHeroColumnItemList[i]["name"] as String
                        TableRow {
                            TableCell {
                                +name
                            }
                            TableCell {
                                if (props.draftHeroColumnItemList[i]["mainImage"]) {
                                    ReactHTML.img {
                                        css {
                                            borderRadius = 8.px
                                            width = 150.px
                                            padding = 5.px
                                        }
                                        src = props.draftHeroColumnItemList[i]["mainImage"]
                                    }
                                }
                            }
                            TableCell {
                                if (props.draftHeroColumnItemList[i]["icon"]) {
                                    ReactHTML.img {
                                        css {
                                            borderRadius = 8.px
                                            width = 150.px
                                            padding = 5.px
                                        }
                                        src = props.draftHeroColumnItemList[i]["icon"]
                                    }
                                }
                            }
                            DeleteSubmitLocalButtonBox {
                                type = "hero"
                                draftItemName = name
                                userName = currentUser
                                mainImage = props.draftHeroColumnItemList[i]["mainImage"]
                                icon = props.draftHeroColumnItemList[i]["icon"]
                                refreshImage = props.refreshImage
                            }
                        }
                    }
                }

            }
        }
    }

}


val DraftColumnsWeapon = FC<DraftColumnsItemProps> { props ->
    val currentUser = props.userName

    div {
        TableContainer {
            Table {
                ariaLabel = "simple-table"
                WeaponColumnHeader()
                TableBody {
                    for (i in 0 until props.draftWeaponColumnItemList.size) {
                        val name = props.draftWeaponColumnItemList[i]["name"] as String
                        TableRow {
                            TableCell {
                                +name
                            }
                            TableCell {
                                if (props.draftWeaponColumnItemList[i]["mainImage"]) {
                                    ReactHTML.img {
                                        css {
                                            borderRadius = 8.px
                                            width = 150.px
                                            padding = 5.px
                                        }
                                        src = props.draftWeaponColumnItemList[i]["mainImage"]
                                    }
                                }
                            }
                            TableCell {
                                if (props.draftWeaponColumnItemList[i]["entireIcon"]) {
                                    ReactHTML.img {
                                        css {
                                            borderRadius = 8.px
                                            width = 150.px
                                            padding = 5.px
                                        }
                                        src = props.draftWeaponColumnItemList[i]["entireIcon"]
                                    }
                                }
                            }
                            TableCell {
                                if (props.draftWeaponColumnItemList[i]["brokenIcon"]) {
                                    ReactHTML.img {
                                        css {
                                            borderRadius = 8.px
                                            width = 150.px
                                            padding = 5.px
                                        }
                                        src = props.draftWeaponColumnItemList[i]["brokenIcon"]
                                    }
                                }
                            }
                            DeleteSubmitLocalButtonBox {
                                type = "weapon"
                                draftItemName = name
                                userName = currentUser
                                mainImage = props.draftWeaponColumnItemList[i]["mainImage"]
                                entireIcon = props.draftWeaponColumnItemList[i]["entireIcon"]
                                brokenIcon = props.draftWeaponColumnItemList[i]["brokenIcon"]
                                refreshImage = props.refreshImage
                            }
                        }
                    }
                }

            }
        }
    }

}

val DeleteSubmitLocalButtonBox = FC<DraftColumnsItemProps> { props ->
    val handleLocalDeleteClick = { _: MouseEvent<*, *> ->
        deleteDraftRequest(props.draftItemName, props.type, props.userName)
        props.refreshImage((1 .. 100).random())
    }
    val handleSubmitClick = { _: MouseEvent<*, *> ->
        submitDraftRequest(
            props.draftItemName,
            props.type,
            props.userName
        )
        props.refreshImage((1 .. 100).random())
    }
    Box {
        sx {
            gap = Gap.normal
        }
        ButtonStyled {
            action = handleLocalDeleteClick
            buttonText = "Delete draft"
        }

        ButtonStyled {
            action = handleSubmitClick
            buttonText = "Submit draft"
        }

    }

}

