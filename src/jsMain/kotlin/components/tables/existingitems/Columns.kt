package components.tables.existingitems

import components.tables.HeroColumnHeader
import components.tables.WeaponColumnHeader
import csstype.px
import emotion.react.css
import mui.material.*
import react.FC
import react.Props
import react.dom.aria.ariaLabel
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import react.router.dom.Link


external interface ColumnsItemProps : Props {
    var heroColumnItemList: Array<dynamic>
    var weaponColumnItemList: Array<dynamic>
    var userName: String
}


val ColumnsHero = FC<ColumnsItemProps> { props ->

    div {
        TableContainer {
            Table {
                ariaLabel = "simple-table"
                HeroColumnHeader()

                TableBody {
                    for (i in 0 until props.heroColumnItemList.size) {
                        val name = props.heroColumnItemList[i]["name"] as String
                        TableRow {
                            TableCell {
                                +name
                            }
                            TableCell {
                                if (props.heroColumnItemList[i]["mainImage"]) {
                                    img {
                                        css {
                                            borderRadius = 8.px
                                            width = 150.px
                                            padding = 5.px
                                        }
                                        src = props.heroColumnItemList[i]["mainImage"]
                                    }
                                }
                            }
                            TableCell {
                                if (props.heroColumnItemList[i]["icon"]) {
                                    img {
                                        css {
                                            borderRadius = 8.px
                                            width = 150.px
                                            padding = 5.px
                                        }
                                        src = props.heroColumnItemList[i]["icon"]
                                    }
                                }
                            }
                            when (props.userName) {
                                "drawer", "drawer2" -> {
                                    TableCell {

                                        Link {
                                            to = "/hero/$name"
                                            +"Go to $name edit page"
                                        }
                                    }
                                }

                                "designer" -> {
                                    TableCell {
                                        +"Link for drawer: /#/hero/$name"
                                    }
                                }

                                else -> {
                                    TableCell {
                                        +"No actions for current user"
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

}


val ColumnsWeapon = FC<ColumnsItemProps> { props ->
    div {
        TableContainer {
            Table {
                ariaLabel = "simple-table"
                WeaponColumnHeader()
                TableBody {
                    for (i in 0 until props.weaponColumnItemList.size) {
                        val name = props.weaponColumnItemList[i]["name"] as String
                        TableRow {
                            TableCell {
                                +name
                            }
                            TableCell {
                                if (props.weaponColumnItemList[i]["mainImage"]) {
                                    img {
                                        css {
                                            borderRadius = 8.px
                                            width = 150.px
                                            padding = 5.px
                                        }
                                        src = props.weaponColumnItemList[i]["mainImage"]
                                    }
                                }
                            }
                            TableCell {
                                if (props.weaponColumnItemList[i]["entireIcon"]) {
                                    img {
                                        css {
                                            borderRadius = 8.px
                                            width = 150.px
                                            padding = 5.px
                                        }
                                        src = props.weaponColumnItemList[i]["entireIcon"]
                                    }
                                }
                            }
                            TableCell {
                                if (props.weaponColumnItemList[i]["brokenIcon"]) {
                                    img {
                                        css {
                                            borderRadius = 8.px
                                            width = 150.px
                                            padding = 5.px
                                        }
                                        src = props.weaponColumnItemList[i]["brokenIcon"]
                                    }
                                }
                            }
                            when (props.userName) {
                                "drawer", "drawer2" -> {
                                    TableCell {

                                        Link {
                                            to = "/weapon/$name"
                                            +"Go to $name edit page"
                                        }
                                    }
                                }

                                "designer" -> {
                                    TableCell {
                                        +"Link for drawer: /#/weapon/$name"
                                    }
                                }

                                else -> {
                                    TableCell {
                                        +"No actions for current user"
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

